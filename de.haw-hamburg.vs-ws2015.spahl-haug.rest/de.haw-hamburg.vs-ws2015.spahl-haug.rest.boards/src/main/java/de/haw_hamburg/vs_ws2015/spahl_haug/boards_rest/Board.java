package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private List<Field> fields;
    // Map<PlayerId, PositionId>
    private Map<Integer, Integer> positions;

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

    public Map<Integer, Integer> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        return "Board{" +
                "fields=" + fields +
                ", positions=" + positions +
                '}';
    }
}
