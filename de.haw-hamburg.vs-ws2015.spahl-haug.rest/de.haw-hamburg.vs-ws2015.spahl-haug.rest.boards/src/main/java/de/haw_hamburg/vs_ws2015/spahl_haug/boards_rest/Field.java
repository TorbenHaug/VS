package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.ArrayList;
import java.util.Iterator;
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

    synchronized public void setPlayer(Player player) {
        players.add(player);
    }

    synchronized public void removePlayer(String playerID) {
        for(Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
            Player player = iterator.next();
            if(player.getId().equals(playerID)) {
                iterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "Field{" +
                "place=" + place +
                ", players=" + players +
                '}';
    }
}
