package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto;

import java.util.List;

public class EventsOutDTO {
    private List<String> events;

    public EventsOutDTO(List<String> events) {
        this.events = events;
    }

    public List<String> getEvents() {
        return events;
    }
}
