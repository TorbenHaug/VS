package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.ArrayList;
import java.util.List;


public class Field {
    private Place place;
    private List<Player> players;

    public Field(Place placeName) {
        players = new ArrayList<>();
        place = placeName;
    }

    public Place getPlace() {
        return place;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayer(long playerId) {
        players.add(new Player(playerId));
    }
}
