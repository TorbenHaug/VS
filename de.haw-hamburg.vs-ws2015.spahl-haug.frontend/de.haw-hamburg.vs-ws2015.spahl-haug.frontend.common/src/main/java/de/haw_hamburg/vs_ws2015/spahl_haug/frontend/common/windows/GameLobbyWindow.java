package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.api.layout.NullLayout;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.controller.WindowAdapter;
import org.jowidgets.tools.widgets.base.Frame;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.BeanTableModel;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.GameList;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.GameTableRenderer;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.PlayerTableRenderer;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class GameLobbyWindow extends Frame implements IMyFrame{
	private final IButtonBluePrint readyBp;
	private final IButton ready;
	private final BeanTableModel<Player> model;
	private final ITableBluePrint tableBp;
	private final ITable table;
	private final IGameLobbyActions lobbyActions;
	private final IButtonBluePrint exitGameBp;
	private final IButton exitGame;
	private final Thread refreshThread;
	private final RestTemplate template = new RestTemplate();
	private final String gameURI;
	private Game game;
	private final IUiThreadAccess uiThreadAccess;
	private final String gameService;

	public GameLobbyWindow(final String userName, final String gameURI, final IGameLobbyActions lobbyActions, final String gameService) {
		super("GameLobby - " + userName + " - " + gameURI);
		this.gameURI = gameURI;
		this.lobbyActions = lobbyActions;
		this.gameService = gameService;

		game  = new Game();

		setLayout(NullLayout.get());
		setSize(1024, 768);

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed() {
				refreshThread.interrupt();
				//GameLobbyWindow.this.lobbyActions.closeWindow();
			}
		});
		readyBp = BPF.button().setText("Set Ready");
		ready = getFrame().add(readyBp);
		ready.setSize(new Dimension(150, 30));
		ready.setPosition(10,50);
		ready.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				GameLobbyWindow.this.lobbyActions.ready(gameURI);

			}
		});

		exitGameBp = BPF.button().setText("Leave Game");
		exitGame = getFrame().add(exitGameBp);
		exitGame.setSize(new Dimension(150, 30));
		exitGame.setPosition(10,130);
		exitGame.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				refreshThread.interrupt();
				try {
					GameLobbyWindow.this.lobbyActions.closeWindow();
				} catch (final RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		model = new BeanTableModel<Player>(new PlayerTableRenderer());
		tableBp = BPF.table(model);
		table = getFrame().add(tableBp);
		table.setEditable(true);
		table.setPosition(200, 50);
		table.setSize(800, 750);

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

	protected void updateGames(){
		final Game tmpGame = getGame(gameURI);
		final List<Player> oldPlayers = new ArrayList<Player>(game.getPlayers());
		oldPlayers.removeAll(tmpGame.getPlayers());
		final List<Player> newPlayers = new ArrayList<Player>(tmpGame.getPlayers());
		newPlayers.removeAll(game.getPlayers());
		removePlayers(oldPlayers);
		addPlayers(newPlayers);
		game = tmpGame;
		boolean allReady = true;
		for(final Player player: game.getPlayers()){
			allReady &= player.isReady();
		}
		if(allReady){
			final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
			new Thread(){
				@Override
				public void run() {
					uiThreadAccess.invokeLater(new Runnable() {

						@Override
						public void run() {
							lobbyActions.startGame(gameURI);

						}
					});

				}

			}.start();

		}

	}

	private Game getGame(final String Id){
		try {
			return template.getForObject(gameService + "/" + Id, Game.class);
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
			model.addBean(player, false);
		}
	}

	private void removePlayers(final List<Player> oldPlayers) {
		for (final Player player: oldPlayers){
			model.removeBean(player);
		}
	}

	public void anounceStartGame() {
		new Thread(){
			@Override
			public void run() {
				uiThreadAccess.invokeLater(new Runnable() {
					@Override
					public void run() {
						refreshThread.interrupt();
						lobbyActions.startGame(gameURI);

					}
				});
			}
		}.start();



	}

	@Override
	public IUiThreadAccess getUIThread() {
		return uiThreadAccess;
	}
}
