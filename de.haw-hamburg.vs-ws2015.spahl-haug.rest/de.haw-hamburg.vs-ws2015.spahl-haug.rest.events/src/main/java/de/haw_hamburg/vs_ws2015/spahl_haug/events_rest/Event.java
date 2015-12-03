package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

public class Event {
    private String name;
    private String reason;
    private String resource;

    public Event(String name, String reason) {
        this.name = name;
        this.reason = reason;
        this.resource = "/events/";
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

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                ", resource='" + resource + '\'' +
                '}';
    }
}
