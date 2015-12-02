package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;


import java.util.List;

public class FieldDTO {
    private String place;
    private List<PlayerDTO> players;

    public FieldDTO(long gameId, int position, List<PlayerDTO> players) {
        this.place = "/boards/" + gameId + "/places/" + position;
        this.players = players;
    }

    public String getPlace() {
        return place;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }
}
