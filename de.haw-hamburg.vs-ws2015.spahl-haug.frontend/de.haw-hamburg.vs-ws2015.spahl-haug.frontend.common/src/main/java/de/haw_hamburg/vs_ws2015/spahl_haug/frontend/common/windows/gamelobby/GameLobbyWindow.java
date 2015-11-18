package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.gamelobby;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.jowidgets.api.layout.NullLayout;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.model.table.SimpleTableModel;
import org.jowidgets.tools.model.table.SimpleTableModelBuilder;
import org.jowidgets.tools.model.table.TableCell;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.BeanTableModel;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.PlayerTableRenderer;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.AbstractWindow;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;

public class GameLobbyWindow extends AbstractWindow{

	private final IButtonBluePrint readyBp;
	private final IButton readyGame;
	private final RestTemplate template = new RestTemplate();
	private final IServiceRepository serviceRepository;
	private final Thread updater;
	private Game game = new Game();

	final BeanTableModel<Player> model;

	public GameLobbyWindow(final IApplicationLifecycle lifecycle, final long gameId, final String username, final IServiceRepository serviceRepositiory, final IUiThreadAccess threadAccess) {
		super(true, new Dimension(1024, 768), lifecycle);
		this.serviceRepository = serviceRepositiory;
		game.setPlayers(new ArrayList<Player>());
		getFrame().setLayout(NullLayout.get());
		getFrame().setTitle("Monopoly - GameLobby - " + gameId);
		readyBp = BPF.button().setText("Ready");
		readyGame = getFrame().add(readyBp);
		readyGame.setSize(new Dimension(150, 30));
		readyGame.setPosition(10,60);
		readyGame.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				try {
					final String url = serviceRepository.getService("gamesldt") + "/" + gameId + "/players/" + username + "/ready";
					System.out.println(url);
					template.put( url, null);
				} catch (final RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		model = new BeanTableModel<Player>(new PlayerTableRenderer());
		final ITableBluePrint tableBp = BPF.table(model);
		final ITable table = getFrame().add(tableBp);
		table.setEditable(true);

		table.setPosition(10, 150);
		table.setSize(800, 600);

		getFrame().setVisible(true);
		this.updater = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean running = true;
				while(running){
					try {
						Thread.sleep(1000);
						threadAccess.invokeLater(new Runnable() {
							@Override
							public void run() {
								final Game tmpGame = getGame(gameId);
								final List<Player> oldPlayers = new ArrayList<Player>(game.getPlayers());
								oldPlayers.removeAll(tmpGame.getPlayers());
								final List<Player> newPlayers = new ArrayList<Player>(tmpGame.getPlayers());
								newPlayers.removeAll(game.getPlayers());
								removePlayers(oldPlayers);
								addPlayers(newPlayers);
								game = tmpGame;
							}
						});
					} catch (final InterruptedException e) {
						running = false;
					}
				}

			}
		});
		updater.start();

	}

	private Game getGame(final long Id){
		try {
			return template.getForObject(serviceRepository.getService("gamesldt") + "/" + Id, Game.class);
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
}
