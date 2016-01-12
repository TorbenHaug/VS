package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.jowidgets.api.image.IImage;
import org.jowidgets.api.image.ImageFactory;
import org.jowidgets.api.layout.NullLayout;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.controller.WindowAdapter;
import org.jowidgets.tools.widgets.base.Frame;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.sun.jersey.core.impl.provider.header.NewCookieProvider;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.PlayersDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.PostRollDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.RollDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class GameWindow extends Frame implements IMyFrame{
	private final IButtonBluePrint rollBp;
	private final IButton roll;
	private final IGameActions lobbyActions;
	private final IButtonBluePrint exitGameBp;
	private final IButton exitGame;
	//	private final Thread refreshThread;
	private final RestTemplate template = new RestTemplate();
	private final String gameURI;
	private final List<PlayerPosition> players = new ArrayList<PlayerPosition>();
	private final Map<String, PlayerInfo> playerInfos = new ConcurrentHashMap<String,PlayerInfo>();
	private PlayersDTO boardPlayer;
	private final IIcon gamefield;
	private final List<String> colors = new ArrayList<String>(Arrays.asList("black", "blue", "green", "purple","red","yellow"));
	private final String boardServiceAdress;
	private final String gameServiceAdress;
	private final IUiThreadAccess uiThreadAccess;
	private final String diceServiceAdress;
	private final IButtonBluePrint buyFieldBp;
	private final IButton buyField;
	private final IButtonBluePrint endTurnBp;
	private final IButton endTurn;
	private final String brokerService;
	private final String bankServiceAdress;

	public GameWindow(final String userName, final String gameURI, final IGameActions lobbyActions, final String gameServiceAdress, final String boardServiceAdress, final String diceServiceAdress, final String brokerService, final String bankServiceAdress) {
		super("GameWindow - " + userName + " - " + gameURI);
		this.gameURI = gameURI;
		this.lobbyActions = lobbyActions;
		this.gameServiceAdress = gameServiceAdress;
		this.boardServiceAdress = boardServiceAdress;
		this.diceServiceAdress = diceServiceAdress;
		this.brokerService = brokerService;
		this.bankServiceAdress = bankServiceAdress;
		boardPlayer  = new PlayersDTO();

		setLayout(NullLayout.get());
		setSize(1024, 768);

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed() {
				//				refreshThread.interrupt();
				//GameWindow.this.lobbyActions.closeWindow();
			}
		});

		rollBp = BPF.button().setText("Roll");
		roll = getFrame().add(rollBp);
		roll.setSize(new Dimension(150, 30));
		roll.setPosition(10,50);
		roll.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				roll.setEnabled(false);
				final String turnAdress = gameServiceAdress + "/" + gameURI + "/players/turn";
				try {
					System.out.println(turnAdress);
					final ResponseEntity<String> mutex = template.exchange(turnAdress + "?player=" + userName, HttpMethod.PUT, null, String.class);
				} catch(final RestClientException e) {
					System.out.println("cannot aquire mutex");
				}
				try{
					final String roolAdress = diceServiceAdress;
					final ResponseEntity<RollDTO> roll1 = template.exchange(roolAdress, HttpMethod.GET, null, RollDTO.class);
					final ResponseEntity<RollDTO> roll2 = template.exchange(roolAdress, HttpMethod.GET, null, RollDTO.class);
					final PostRollDTO postRollDTO = new PostRollDTO(roll1.getBody(), roll2.getBody());
					System.out.println(postRollDTO);
					final String[] split = gameURI.split("/");
					final String gameId = split[split.length-1];
					final String rollAdress = boardServiceAdress + "/"  + gameId + "/players/" + userName + "/roll";
					template.postForLocation(rollAdress, postRollDTO);
				}catch(final RestClientException e){
					System.out.println("Somthing went wrong via rolling");
				}
				roll.setEnabled(false);
				buyField.setEnabled(true);
				endTurn.setEnabled(true);

			}
		});
		roll.setEnabled(false);

		buyFieldBp = BPF.button().setText("Buy Field");
		buyField = getFrame().add(buyFieldBp);
		buyField.setSize(new Dimension(150, 30));
		buyField.setPosition(10,90);
		buyField.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				//				refreshThread.interrupt();
				final String[] split = gameURI.split("/");
				final String gameId = split[split.length - 1];
				final String uri = brokerService + "/" + gameId + "/places/" + playerInfos.get(userName).getPos() + "/owner";
				System.out.println("Buy the Place: " + uri);
				try{
					final Player player = new Player();
					player.setId(userName);
					template.postForLocation(uri, player);
				}catch(final RestClientException e){
					System.out.println("Not for sale");
				}
				buyField.setEnabled(false);
			}
		});
		buyField.setEnabled(false);

		endTurnBp = BPF.button().setText("End Turn");
		endTurn = getFrame().add(endTurnBp);
		endTurn.setSize(new Dimension(150, 30));
		endTurn.setPosition(10,130);
		endTurn.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				endTurn.setEnabled(false);
				buyField.setEnabled(false);
				try{
					final String turnAdress = gameURI + "/players/turn";
					final ResponseEntity<String> mutex = template.exchange(turnAdress, HttpMethod.DELETE, null, String.class);
					final String readyAdress = gameURI + "/players/" + userName + "/ready";
					try{
						template.put(readyAdress, null);
					}catch(final RestClientException e){
						System.out.println(e.getMessage() + " " + readyAdress);
					}
				}catch(final RestClientException e){
					System.out.println("Cannot ReleaseMutex");
				}
			}
		});
		endTurn.setEnabled(false);

		exitGameBp = BPF.button().setText("Leave Game");
		exitGame = getFrame().add(exitGameBp);
		exitGame.setSize(new Dimension(150, 30));
		exitGame.setPosition(10,170);
		exitGame.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				//				refreshThread.interrupt();
				try {
					try{
						final String turnAdress = gameServiceAdress + "/" + gameURI + "/players/turn";
						final ResponseEntity<String> mutex = template.exchange(turnAdress, HttpMethod.DELETE, null, String.class);
						final String readyAdress = gameServiceAdress + "/" + gameURI + "/players/" + userName + "/ready";
						try{
							template.put(readyAdress, null);
						}catch(final RestClientException e){
							System.out.println(e.getMessage() + " " + readyAdress);
						}
					}catch(final RestClientException e){
						System.out.println("Cannot ReleaseMutex");
					}
					GameWindow.this.lobbyActions.closeWindow();
				} catch (final RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		createPlayerPositions();

		int playerInfoY = 200;
		final Game initialGame = getGame(gameURI);
		for(final Player player: initialGame.getPlayers()){
			final PlayerInfo playerInfo = new PlayerInfo(this, player.getId(), colors.remove(0));
			playerInfo.setPosition(0, playerInfoY);
			playerInfo.setVisible(true);
			playerInfoY += 30;
			playerInfos.put(player.getId(),playerInfo);
			playerInfo.setPos(0);

			setPlayerFromTo(playerInfo.getColor(), playerInfo.getPos(), playerInfo.getPos());
		}



		final URL gamefieldurl = GameWindow.class.getClassLoader().getResource("gamefield.jpg");
		final IImage gamefieldImage= ImageFactory.createImage(gamefieldurl);
		gamefield = add(BPF.icon(gamefieldImage));
		gamefield.setSize(gamefieldImage.getSize());
		gamefield.setPosition(200, 50);

		uiThreadAccess = Toolkit.getUiThreadAccess();

		setVisible(true);
		update();
	}

	private void setPlayerFromTo(final String color, final int oldPos, final int newPos) {
		if(color.equals("black")){
			players.get(oldPos).setBlackVisible(false);
			players.get(newPos).setBlackVisible(true);
		}
		else if(color.equals("blue")){
			players.get(oldPos).setBlueVisible(false);
			players.get(newPos).setBlueVisible(true);
		}
		else if(color.equals("green")){
			players.get(oldPos).setGreenVisible(false);
			players.get(newPos).setGreenVisible(true);
		}
		else if(color.equals("purple")){
			players.get(oldPos).setPurpleVisible(false);
			players.get(newPos).setPurpleVisible(true);
		}
		else if(color.equals("red")){
			players.get(oldPos).setRedVisible(false);
			players.get(newPos).setRedVisible(true);
		}
		else if(color.equals("yellow")){
			players.get(oldPos).setYellowVisible(false);
			players.get(newPos).setYellowVisible(true);
		}
	}

	private void createPlayerPositions() {
		int x= 820;
		int y= 680;
		players.add(createPlayerPosition(x + 30,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58,y,false));
		players.add(createPlayerPosition(x=x-58-30,y,false));
		players.add(createPlayerPosition(x,y=y-75,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57,true));
		players.add(createPlayerPosition(x,y=y-57-40,false));
		players.add(createPlayerPosition(x=x+58+30,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58,y,false));
		players.add(createPlayerPosition(x=x+58+30,y,false));
		players.add(createPlayerPosition(x = x - 13,y=y+57+40,true));
		players.add(createPlayerPosition(x,y=y+57,true));
		players.add(createPlayerPosition(x,y=y+57,true));
		players.add(createPlayerPosition(x,y=y+57,true));
		players.add(createPlayerPosition(x,y=y+57,true));
		players.add(createPlayerPosition(x,y=y+57,true));
		players.add(createPlayerPosition(x,y=y+57,true));
		players.add(createPlayerPosition(x,y=y+57,true));
		players.add(createPlayerPosition(x,y=y+57,true));

	}

	private PlayerPosition createPlayerPosition(final int x, final int y, final boolean rotated) {
		final PlayerPosition position = new PlayerPosition(this);
		position.setPosition(x, y);
		if(rotated){
			position.rotate();
		}
		position.setVisible(true);
		return position;
	}

	protected void updatePositions(){
		final String[] split = gameURI.split("/");
		final String gameId = split[split.length-1];
		final PlayersDTO tmpPlayers = getBoardPlayer(gameId);
		System.out.println("Function updatePositions: Old Player before update " + tmpPlayers);
		for(final Player newPlayer: tmpPlayers.getPlayers()){
			final PlayerInfo playerInfo = playerInfos.get(newPlayer.getId());
			if(playerInfo.getPos() != newPlayer.getPosition()){
				setPlayerFromTo(playerInfo.getColor(),playerInfo.getPos(), newPlayer.getPosition());
				playerInfo.setPos(newPlayer.getPosition());
			}
		}
		boardPlayer = tmpPlayers;

	}

	private Game getGame(final String gameURI){
		try {
			return template.getForObject(gameURI, Game.class);
		} catch (final RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private PlayersDTO getBoardPlayer(final String id){
		try {
			System.out.println(boardServiceAdress + "/" + id);
			return template.getForObject(boardServiceAdress + "/" + id, PlayersDTO.class);
		} catch (final RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public void anounceTurn() {
		uiThreadAccess.invokeLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("EnableRollButton");
				roll.setEnabled(true);

			}
		});
	}

	@Override
	public IUiThreadAccess getUIThread() {
		return uiThreadAccess;
	}

	public void update() {
		uiThreadAccess.invokeLater(new Runnable() {
			@Override
			public void run() {
				updatePositions();
			}
		});
	}

	public void updateMoney(final String playerID, final String playerBankURI){
		uiThreadAccess.invokeLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("Update Money: " + playerBankURI);
				final ResponseEntity<Integer> amount = template.getForEntity(playerBankURI, Integer.class);
				playerInfos.get(playerID).updateMoney(amount.getBody());
			}
		});
	}


}
