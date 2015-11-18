package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

	private final List<Field> fields;
	// Map<PlayerId, PositionId>
	private final Map<Long, Integer> positions;

	public Board() {
		final List<Field> fieldList = new ArrayList<>();
		for ( final Place place : Place.values()) {
			fieldList.add(new Field(place));
		}

		this.fields = fieldList;
		this.positions = new HashMap<>();
	}

	public List<Field> getFields() {
		return fields;
	}

	public Map<Long, Integer> getPositions() {
		return positions;
	}

	public Player getPlayer(final long id) throws PlayerDoesntExistsException{
		if(positions.containsKey(id)){
			return new Player(id);
		}else{
			throw new PlayerDoesntExistsException("Player Doesnt Exists");
		}
	}

	public void addPositions(final long playerId, final int positionId) {
		if(isPositionOnBoard(positionId)) {
			positions.put(playerId, positionId);
			for(final Field f : fields) {
				if (f.getPlace().getPosition() == positionId) {
					f.setPlayer(playerId);
				}
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
	public int getPosition(final long playerId) {
		return positions.get(playerId);
	}


	public void removePlayer(final long playerID){
		positions.remove(playerID);
	}

	@Override
	public String toString() {
		return "Board{" +
				"fields=" + fields +
				", positions=" + positions +
				'}';
	}
}
