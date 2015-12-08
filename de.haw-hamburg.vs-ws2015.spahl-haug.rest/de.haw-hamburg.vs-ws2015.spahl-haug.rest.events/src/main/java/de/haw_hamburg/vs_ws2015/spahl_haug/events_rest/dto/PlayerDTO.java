package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto;

public class PlayerDTO {
    private String id;
    private PlaceDTO place;

    public PlayerDTO(){}

    public PlayerDTO(String playerId, PlaceDTO place) {
        this.id = playerId;
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public PlaceDTO getPlace() {
        return place;
    }
}
