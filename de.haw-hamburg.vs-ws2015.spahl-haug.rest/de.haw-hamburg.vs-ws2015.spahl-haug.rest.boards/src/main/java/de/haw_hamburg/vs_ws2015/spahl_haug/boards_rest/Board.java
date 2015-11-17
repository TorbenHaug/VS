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
        String los = "Los";
        String einkommensteuer = "Einkommensteuer";
        List<Field> fieldList = new ArrayList<>();
        fieldList.add(new Field(los));
        fieldList.add(new Field(einkommensteuer));

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
        positions.put(playerId, positionId);
    }

    public void removePlayer(long playerID){
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
