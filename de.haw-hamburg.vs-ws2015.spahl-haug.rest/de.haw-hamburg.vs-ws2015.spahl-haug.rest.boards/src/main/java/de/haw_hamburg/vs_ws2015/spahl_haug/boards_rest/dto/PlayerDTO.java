package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

public class PlayerDTO {
    private String id;
    private  String uri;
    private String place;
    private int position;

    public PlayerDTO() {

    }

    public PlayerDTO(String id, long gameId, int position, String uri) {
        this.place = "/boards/" + gameId +"/places/" + position;
        this.id = id;
        this.position = position;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }
}
