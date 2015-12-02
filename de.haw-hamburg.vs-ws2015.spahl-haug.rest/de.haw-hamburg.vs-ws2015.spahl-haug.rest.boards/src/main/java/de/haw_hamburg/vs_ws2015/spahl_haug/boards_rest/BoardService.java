package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PositionNotOnBoardException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardService {
    // Map<gameId, Board>
	private Map<Long, Board> boards;

	public BoardService(){
        this.boards = new HashMap<>();
	}

	@JsonIgnore
	public Board getBoard(final long gameID) {
		return boards.get(gameID);
	}

	public void createBoard(final long gameID) {
		final Board board = new Board();
        this.boards.put(gameID, board);
    }

	public Map<Long, Board> getBoards() {
		return boards;
	}

	public void deleteBoard(final long gameID) {
		boards.remove(gameID);
	}

	public List<Place>  getPlaces(final long gameID) {
		final List<Place> placesMap = new ArrayList<>();

		for(final Field field : boards.get(gameID).getFields()) {
			final Place place = field.getPlace();
			placesMap.add(place);
		}
		return placesMap;
	}

	public void placePlayer(final long gameID, final String playerID) throws PositionNotOnBoardException, PlayerDoesntExistsException {
        boards.get(gameID).setPlayer(playerID);
    }

    public Board placePlayer(final long gameID, final String playerID, int numOfPosMoves) throws PositionNotOnBoardException, PlayerDoesntExistsException {
        Board board = boards.get(gameID);
		board.placePlayerOnPos(playerID, numOfPosMoves);
		return board;
    }

	public void removePlayerFromBoard(final long gameID, final String playerID) {
		boards.get(gameID).removePlayer(playerID);
	}

	public Player getPlayerPosition(final long gameID, final String playerID) throws PlayerDoesntExistsException {
		return boards.get(gameID).getPosition(playerID);
	}

	@JsonIgnore
	public Player getPlayer(final long gameID, final String playerID) throws PlayerDoesntExistsException {
		return getBoard(gameID).getPlayer(playerID);
	}

    public List<Player> getPlayerFromBoard(long gameID) {
        return boards.get(gameID).getPlayers();
    }
}
