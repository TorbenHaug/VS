package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

/**
 * Created by Louisa on 01.12.2015.
 */
public class PlayerDTO {
    private String id;
    private String place;
    private int position;

    public PlayerDTO() {

    }

    public PlayerDTO(String id, long gameId, int position) {
        this.place = "/boards/" + gameId +"/places/" + position;
        this.id = id;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getPlace() {
        return place;
    }
}
