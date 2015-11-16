package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.lobby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.model.table.SimpleTableModel;
import org.jowidgets.tools.widgets.blueprint.BPF;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.AbstractWindow;

public class LobbyWindow extends AbstractWindow{

	private final IButtonBluePrint createGameBp;
	private final IButton createGame;
	private final IButtonBluePrint enterGameBp;
	private final IButton enterGame;


	public LobbyWindow(final String userName) {
		super(false, new Dimension(1024, 768));
		final List<Game> games = new ArrayList<Game>();
		for(int i = 0; i<20; i++){
			final Game game = new Game();
			game.setGameid(i);
			games.add(game);
		}

		final String buttonCC = "growx, w 0::";
		getFrame().setTitle("Monopoly - Lobby -" + userName);
		getFrame().setLayout(new MigLayoutDescriptor("wrap", "[150][grow, 0::]", "[][]"));
		createGameBp = BPF.button().setText("New Game");
		createGame = getFrame().add(createGameBp,buttonCC);
		final IComboBoxSelectionBluePrint<Game> gameListBp = BPF.comboBoxSelection(games);
		final IComboBox<Game> gameList = getFrame().add(gameListBp, "span 1 2");
		gameList.setSize(800, 600);
		enterGameBp = BPF.button().setText("Enter Game");
		enterGame = getFrame().add(enterGameBp,buttonCC);
		createGame.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				games.add(new Game());
				gameList.setElements(games);
			}
		});
		getFrame().setVisible(true);

	}

}
