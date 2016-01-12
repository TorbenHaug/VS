package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

public class PlaceDTO {
    private String name;
    private String uri;

    public PlaceDTO(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
