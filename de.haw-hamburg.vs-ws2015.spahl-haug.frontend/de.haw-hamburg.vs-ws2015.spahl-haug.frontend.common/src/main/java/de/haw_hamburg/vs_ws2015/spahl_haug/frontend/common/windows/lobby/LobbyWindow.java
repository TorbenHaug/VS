package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.lobby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.model.table.SimpleTableModel;
import org.jowidgets.tools.threads.UiSingleThreadAccess;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.GameList;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.AbstractWindow;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;

public class LobbyWindow extends AbstractWindow{

	private final IComboBoxSelectionBluePrint<Game> gameListBp;
	private final IComboBox<Game> gameList;
	private final IButtonBluePrint createGameBp;
	private final IButton createGame;
	private final IButtonBluePrint enterGameBp;
	private final IButton enterGame;

	public LobbyWindow(final String userName,final ILobbyActions lobbyActions, final IApplicationLifecycle lifecycle) {
		super(false, new Dimension(1024, 768),lifecycle);
		final List<Game> games = new ArrayList<Game>();

		final String buttonCC = "growx, w 0::";
		getFrame().setTitle("Monopoly - Lobby -" + userName);
		getFrame().setLayout(new MigLayoutDescriptor("wrap", "[150][grow, 0::]", "[grow, 0::][]"));
		createGameBp = BPF.button().setText("New Game");
		createGame = getFrame().add(createGameBp,buttonCC);
		createGame.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				lobbyActions.createNewGame();
			}
		});

		gameListBp = BPF.comboBoxSelection(games);
		gameList = getFrame().add(gameListBp, "growx, span 1 2");

		enterGameBp = BPF.button().setText("Enter Game");
		enterGame = getFrame().add(enterGameBp);
		enterGame.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				lobbyActions.enterGame(gameList.getValue().getGameid());
			}
		});
		getFrame().setVisible(true);
	}

	public void updateGames(final GameList games){
		final Game game = gameList.getValue();

		gameList.setElements(games.getGames());
		gameList.setValue(game);

	}

}
