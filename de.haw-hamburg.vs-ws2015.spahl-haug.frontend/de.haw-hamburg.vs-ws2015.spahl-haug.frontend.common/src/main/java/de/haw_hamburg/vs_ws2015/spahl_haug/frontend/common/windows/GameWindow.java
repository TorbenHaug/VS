package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Player;

import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class GameWindow extends Frame{
	private final IButtonBluePrint rollBp;
	private final IButton roll;
	private final IGameActions lobbyActions;
	private final IButtonBluePrint exitGameBp;
	private final IButton exitGame;
	private final Thread refreshThread;
	private final RestTemplate template = new RestTemplate();
	private final String gameId;
	private final List<PlayerPosition> players = new ArrayList<PlayerPosition>();
	private final List<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();
	private Game game;
	private final IIcon gamefield;
	private final List<String> colors = new ArrayList<String>(Arrays.asList("black", "blue", "green", "purple","red","yellow"));
	private final String boardServiceAdress;
	private final String gameServiceAdress;
	private final IUiThreadAccess uiThreadAccess;

	public GameWindow(final String userName, final String gameId, final IGameActions lobbyActions, final String gameServiceAdress, final String boardServiceAdress) {
		super("GameLobby - " + userName + " - " + gameId);
		this.gameId = gameId;
		this.lobbyActions = lobbyActions;
		this.gameServiceAdress = gameServiceAdress;
		this.boardServiceAdress = boardServiceAdress;
		game  = new Game();

		setLayout(NullLayout.get());
		setSize(1024, 768);

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed() {
				refreshThread.interrupt();
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
				throw new RuntimeException("Not Yet Implemented");

			}
		});
		roll.setEnabled(false);

		exitGameBp = BPF.button().setText("Leave Game");
		exitGame = getFrame().add(exitGameBp);
		exitGame.setSize(new Dimension(150, 30));
		exitGame.setPosition(10,130);
		exitGame.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				refreshThread.interrupt();
				GameWindow.this.lobbyActions.closeWindow();
			}
		});

		int playerInfoY = 170;
		final Game initialGame = getGame(gameId);
		for(final Player player: initialGame.getPlayers()){
			final PlayerInfo playerInfo = new PlayerInfo(this, player.getId(), colors.remove(0));
			playerInfo.setPosition(0, playerInfoY);
			playerInfo.setVisible(true);
			playerInfoY += 30;
			playerInfos.add(playerInfo);
		}

		createPlayerPositions();

		final URL gamefieldurl = GameWindow.class.getClassLoader().getResource("gamefield.jpg");
		final IImage gamefieldImage= ImageFactory.createImage(gamefieldurl);
		gamefield = add(BPF.icon(gamefieldImage));
		gamefield.setSize(gamefieldImage.getSize());
		gamefield.setPosition(200, 50);

		uiThreadAccess = Toolkit.getUiThreadAccess();

		refreshThread = new Thread(){
			@Override
			public void run() {

				while(!isInterrupted()){
					try {
						Thread.sleep(1000);
						uiThreadAccess.invokeLater(new Runnable() {

							@Override
							public void run() {
								updateGames();
							}
						});
					} catch (final InterruptedException e1) {
						interrupt();
					}
				}
			}
		};

		setVisible(true);
		refreshThread.start();
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

	protected void updateGames(){
		final Game tmpGame = getGame(gameId);
		final List<Player> oldPlayers = new ArrayList<Player>(game.getPlayers());
		oldPlayers.removeAll(tmpGame.getPlayers());
		final List<Player> newPlayers = new ArrayList<Player>(tmpGame.getPlayers());
		newPlayers.removeAll(game.getPlayers());
		removePlayers(oldPlayers);
		addPlayers(newPlayers);
		game = tmpGame;

	}

	private Game getGame(final String id){
		try {
			return template.getForObject(gameServiceAdress + "/" + id, Game.class);
		} catch (final RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void addPlayers(final List<Player> newPlayers) {
		for (final Player player: newPlayers){
			//model.addBean(player, false);
		}
	}

	private void removePlayers(final List<Player> oldPlayers) {
		for (final Player player: oldPlayers){
			//model.removeBean(player);
		}
	}

	public void anounceTurn() {
		try {
			uiThreadAccess.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					roll.setEnabled(true);

				}
			});
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
