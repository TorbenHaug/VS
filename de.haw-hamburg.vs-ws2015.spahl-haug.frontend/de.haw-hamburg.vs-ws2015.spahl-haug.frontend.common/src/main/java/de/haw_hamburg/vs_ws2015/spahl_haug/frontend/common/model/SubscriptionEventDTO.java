package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;


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
