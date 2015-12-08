package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto;


public class EventOutDTO {

    private String type; // Event Type e.g. bank transfer, rent, got to jail, estate transfer
    private String name;
    private String reason;
    private String resource;
    private PlayerDTO player;

    public EventOutDTO() {}

    public EventOutDTO(String type, String name, String reason, String resource, PlayerDTO player) {
        this.type = type;
        this.name = name;
        this.reason = reason;
        this.resource = resource;
        this.player = player;
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

    public String getResource() {
        return resource;
    }

    public PlayerDTO getPlayer() {
        return player;
    }
}
