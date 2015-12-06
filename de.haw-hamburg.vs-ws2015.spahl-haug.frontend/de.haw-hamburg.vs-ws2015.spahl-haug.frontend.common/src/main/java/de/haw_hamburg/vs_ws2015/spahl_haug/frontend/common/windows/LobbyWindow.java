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
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class LobbyWindow extends Frame{
	private final IButtonBluePrint createGameBp;
	private final IButton createGame;
	private final IButtonBluePrint enterGameBp;
	private final IButton enterGame;
	private final BeanTableModel<Game> model;
	private final ITableBluePrint tableBp;
	private final ITable table;
	private final ILobbyActions lobbyActions;
	private final IButtonBluePrint exitGameBp;
	private final IButton exitGame;
	private final Thread refreshThread;
	private GameList gameList = new GameList();
	private final RestTemplate template = new RestTemplate();
	private final ServiceRepository serviceRepository;

	public LobbyWindow(final String userName, final ILobbyActions lobbyActions, final ServiceRepository serviceRepository) {
		super("Lobby - " + userName);
		this.lobbyActions = lobbyActions;
		this.serviceRepository = serviceRepository;

		setLayout(NullLayout.get());
		setSize(1024, 768);

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed() {
				refreshThread.interrupt();
				//LobbyWindow.this.lobbyActions.closeWindow();
			}
		});
		createGameBp = BPF.button().setText("New Game");
		createGame = getFrame().add(createGameBp);
		createGame.setSize(new Dimension(150, 30));
		createGame.setPosition(10,50);
		createGame.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				LobbyWindow.this.lobbyActions.createGame();

			}
		});

		enterGameBp = BPF.button().setText("Enter Game");
		enterGame = getFrame().add(enterGameBp);
		enterGame.setSize(new Dimension(150, 30));
		enterGame.setPosition(10,90);
		enterGame.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				LobbyWindow.this.lobbyActions.enterGame("" + model.getSelectedBean().getGameid());

			}
		});

		exitGameBp = BPF.button().setText("Exit Game");
		exitGame = getFrame().add(exitGameBp);
		exitGame.setSize(new Dimension(150, 30));
		exitGame.setPosition(10,130);
		exitGame.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				refreshThread.interrupt();
				LobbyWindow.this.lobbyActions.closeWindow();
			}
		});

		model = new BeanTableModel<Game>(new GameTableRenderer());
		tableBp = BPF.table(model);
		table = getFrame().add(tableBp);
		table.setEditable(true);
		table.setPosition(200, 50);
		table.setSize(800, 700);

		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();

		refreshThread = new Thread(){
			@Override
			public void run() {

				while(!isInterrupted()){
					try {
						Thread.sleep(1000);
						uiThreadAccess.invokeLater(new Runnable() {

							@Override
							public void run() {
								try {
									final GameList games = template.getForObject(LobbyWindow.this.serviceRepository.getService("gamesldt"), GameList.class);
									if(games != null) {
										updateGames(games);
									}
								} catch (final RestClientException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (final Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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

	protected void updateGames(final GameList games){
		final List<Game> oldGames = new ArrayList<Game>(gameList.getGames());
		oldGames.removeAll(games.getGames());
		final List<Game> newGames = new ArrayList<Game>(games.getGames());
		newGames.removeAll(gameList.getGames());
		removeGames(oldGames);
		addGames(newGames);
		gameList = games;

	}

	private void addGames(final List<Game> newGames) {
		for (final Game game: newGames){
			model.addBean(game, false);
		}

	}

	private void removeGames(final List<Game> newGames) {
		for (final Game game: newGames){
			model.removeBean(game);
		}
	}
}
