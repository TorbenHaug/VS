package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private List<Field> fields;
    // Map<PlayerId, PositionId>
    private Map<Long, Integer> positions;

    public Board() {
        List<Field> fieldList = new ArrayList<>();
        for ( Place place : Place.values()) {
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

    public int getPosition(long playerId) {
        return positions.get(playerId);
    }

    public void addPositions(long playerId, int positionId) {
        if(isPositionOnBoard(positionId)) {
            positions.put(playerId, positionId);
            for(Field f : fields) {
                if (f.getPlace().getPosition() == positionId) {
                    // duplikate noch nicht raus
                    f.setPlayer(playerId);
                }
            }
        }
    }

    private boolean isPositionOnBoard(int position) {
        for (Place place : Place.values()){
            if (position == place.getPosition()) {
                return true;
            }
        }
        return false;
    }

    public void removePlayer(long playerID){
        int position = positions.remove(playerID);
//        System.err.println(position);
        for (Field f : fields) {
            if (f.getPlace().getPosition() == position) {
                System.err.println(f.getPlace());
                System.err.println(f.getPlace().getPosition());
                f.removePlayer(playerID);
            }
        }
    }

    @Override
    public String toString() {
        return "Board{" +
                "fields=" + fields +
                ", positions=" + positions +
                '}';
    }
}
