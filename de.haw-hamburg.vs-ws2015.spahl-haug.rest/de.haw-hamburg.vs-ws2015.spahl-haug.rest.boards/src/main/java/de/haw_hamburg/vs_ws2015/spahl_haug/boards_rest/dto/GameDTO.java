package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.List;


public class GameDTO {
    private long gameid;
    @Ignore
    private List<String> players;

    public long getGameid() {
        return gameid;
    }

//    public List<String> getPlayers() {
//        return players;
//    }
}
