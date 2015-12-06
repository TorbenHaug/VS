package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PositionNotOnBoardException;

import java.util.*;

public class Board {

	private final List<Field> fields;
	// Map<playerId, Player>
	private final Map<String, Player> players;

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
		final Player player = new Player(playerId);
		for(final Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
			final Field f = iterator.next();
			if (f.getPlace().getPosition() == 0) {
				player.setPosition(0);
				player.setPlace(f.getPlace());
				f.setPlayer(player);
			}
		}
		players.put(playerId, player);
	}

	public List<Player> getPlayers() {
		final List<Player> returnList = new ArrayList<>();
		returnList.addAll(players.values());
		return returnList;
	}

	public void placePlayerOnPos(final String playerId, final int numOfPosMoves) throws PositionNotOnBoardException, PlayerDoesntExistsException {
		final int oldPos = players.get(playerId).getPosition();
		final int positionId = (oldPos + numOfPosMoves) % Place.values().length ;
		if(!isPositionOnBoard(positionId)) {
			throw new PositionNotOnBoardException("Position is no position from board");
		}

		final Player player = playerOnBoard(playerId);

		for ( final Iterator<Field> iterator = fields.iterator(); iterator.hasNext(); ) {
			final Field f = iterator.next();
			if (f.getPlace().getPosition() == positionId) {
				player.setPosition(positionId);
				player.setPlace(f.getPlace());
				f.setPlayer(player);
			}
		}

		for ( final Iterator<Field> iterator = fields.iterator(); iterator.hasNext(); ) {
			final Field f = iterator.next();
			if (f.getPlace().getPosition() == oldPos) {
				f.removePlayer(playerId);
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
		final int position = players.get(playerID).getPosition();
		for(final Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
			final Field f = iterator.next();
			if (f.getPlace().getPosition() == position) {
				f.removePlayer(playerID);
			}
		}
		players.remove(playerID);
	}



	private Player playerOnBoard(final String playerId) throws PlayerDoesntExistsException {
		final Player player = players.get(playerId);
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
