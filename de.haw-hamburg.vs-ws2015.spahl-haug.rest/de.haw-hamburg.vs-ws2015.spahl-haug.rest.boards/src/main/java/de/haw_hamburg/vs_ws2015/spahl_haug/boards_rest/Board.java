package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PositionNoOnBoardException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

	private final List<Field> fields;
    // Map<playerId, Player>
	private Map<String, Player> players;

	public Board() {
		final List<Field> fieldList = new ArrayList<>();
		for ( final Place place : Place.values()) {
			fieldList.add(new Field(place));
		}

		this.fields = fieldList;
		this.players = new HashMap<>();
	}

	public List<Field> getFields() {
		return fields;
	}

	public Player getPlayer(final String playerId) throws PlayerDoesntExistsException {
		return playerOnBoard(playerId);
	}

    public void setPlayer(final String playerId) {
        Player player = new Player(playerId);
        for(final Field f : fields) {
            if (f.getPlace().getPosition() == 0) {
                player.setPosition(0);
                player.setPlace(f.getPlace());
                f.setPlayer(player);
            }
        }
        players.put(playerId, player);
    }

    public List<Player> getPlayers() {
        List<Player> returnList = new ArrayList<>();
        returnList.addAll(players.values());
        return returnList;
    }

	public void addPositions(final String playerId, final int positionId) throws PositionNoOnBoardException, PlayerDoesntExistsException {
		if(!isPositionOnBoard(positionId)) {
			throw new PositionNoOnBoardException("Position is no position from board");
		}
		Player player = playerOnBoard(playerId);
		for(final Field f : fields) {
			if (f.getPlace().getPosition() == positionId) {
				player.setPosition(positionId);
				player.setPlace(f.getPlace());
				f.setPlayer(player);
			}
		}
	}

	private boolean isPositionOnBoard(final int position) {
		for (final Place place : Place.values()){
			if (position == place.getPosition()) {
				return true;
			}
		}
		return false;
	}

	public Player getPosition(final String playerId) throws PlayerDoesntExistsException {
		return playerOnBoard(playerId);
	}

    public void removePlayer(final String playerID){
		int position = players.get(playerID).getPosition();
        for (Field f : fields) {
            if (f.getPlace().getPosition() == position) {
                f.removePlayer(playerID);
            }
        }
        players.remove(playerID);
    }



	private Player playerOnBoard(String playerId) throws PlayerDoesntExistsException {
		Player player = players.get(playerId);
		System.err.println("player " + player);
		if(player != null) {
			return player;
		} else {
			throw new PlayerDoesntExistsException("Player is not on board");
		}
	}

	@Override
	public String toString() {
		return "Board{" +
				"fields=" + fields +
				", players=" + players +
				'}';
	}
}
