package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto;

public class SubscriptionDTO {
    private String gameid;
    private String uri;
    private SubscriptionEventDTO event;

    public SubscriptionDTO() {}

    public SubscriptionDTO(String gameid, String uri, SubscriptionEventDTO event) {
        this.gameid = gameid;
        this. uri = uri;
        this.event = event;
    }

    public String getGameid() {
        return gameid;
    }

    public String getUri() {
        return uri;
    }

    public SubscriptionEventDTO getEvent() {
        return event;
    }
}
