package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto;

public class EventDTO {
    private String type; // Event Type e.g. bank transfer, rent, got to jail, estate transfer
    private String name;
    private String reason;

    public EventDTO() {}

    public EventDTO(String type, String name, String reason) {
        this.type = type;
        this.name = name;
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }
}
