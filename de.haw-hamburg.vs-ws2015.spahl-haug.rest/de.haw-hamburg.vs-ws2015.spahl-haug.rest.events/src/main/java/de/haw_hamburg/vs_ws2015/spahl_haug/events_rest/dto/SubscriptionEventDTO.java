package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto;


public class SubscriptionEventDTO {
    private String type;


    public SubscriptionEventDTO(){}

    public SubscriptionEventDTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
