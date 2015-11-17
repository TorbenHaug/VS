package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.ArrayList;
import java.util.List;


public class Field {
    private Place place;
    private List<Player> players;

    public Field(String placeName) {
        players = new ArrayList<>();
        place = new Place(placeName);
    }

    public Place getPlace() {
        return place;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
