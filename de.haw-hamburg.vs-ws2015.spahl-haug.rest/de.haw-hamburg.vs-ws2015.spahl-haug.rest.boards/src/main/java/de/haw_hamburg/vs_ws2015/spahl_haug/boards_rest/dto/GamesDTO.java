package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

import java.util.List;

/**
 * Created by Louisa on 17.11.2015.
 */
public class GamesDTO {

    private List<GameDTO> games;

    public List<GameDTO> getGames() {
        return games;
    }

    @Override
    public String toString() {
        return "GamesDTO{" +
                "games=" + games +
                '}';
    }
}
