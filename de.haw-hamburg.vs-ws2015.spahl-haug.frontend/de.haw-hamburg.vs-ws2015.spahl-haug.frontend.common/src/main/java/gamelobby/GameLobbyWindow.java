package gamelobby;

import org.jowidgets.api.layout.NullLayout;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.AbstractWindow;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;

public class GameLobbyWindow extends AbstractWindow{

	private final IButtonBluePrint readyBp;
	private final IButton readyGame;
	private final RestTemplate template = new RestTemplate();
	private final IServiceRepository serviceRepository;
	private final Thread updater;

	public GameLobbyWindow(final IApplicationLifecycle lifecycle, final long gameId, final String username, final IServiceRepository serviceRepositiory, final IUiThreadAccess threadAccess) {
		super(true, new Dimension(1024, 768), lifecycle);
		this.serviceRepository = serviceRepositiory;
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
					template.put(serviceRepository.getService("gamesldt") + "/" + gameId + "/players/" + username + "/ready" , String.class);
				} catch (final RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		final IComboBoxSelectionBluePrint<Player> playerListBp = BPF.comboBoxSelection(getGame(gameId).getPlayers());
		final IComboBox<Player> playerList = getFrame().add(playerListBp);
		playerList.setPosition(10, 100);
		playerList.setSize(800, 500);
		getFrame().setVisible(true);
		this.updater = new Thread(new Runnable() {

			@Override
			public void run() {
				threadAccess.invokeLater(new Runnable() {
					@Override
					public void run() {
						final Player player = playerList.getValue();

						playerList.setElements(getGame(gameId).getPlayers());
						playerList.setValue(player);
					}
				});

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
}
