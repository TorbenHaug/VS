package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto;

public class PlaceDTO {
    private String name;

    public PlaceDTO(){}

    public PlaceDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
