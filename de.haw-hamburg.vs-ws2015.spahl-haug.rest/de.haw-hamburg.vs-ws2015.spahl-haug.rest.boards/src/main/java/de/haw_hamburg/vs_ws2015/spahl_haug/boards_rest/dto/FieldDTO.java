package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;


public class FieldDTO {
    private PlaceDTO place;

    public FieldDTO(PlaceDTO place) {
        this.place = place;
    }

    public FieldDTO() {
    }

    public PlaceDTO getPlace() {
        return place;
    }
}
