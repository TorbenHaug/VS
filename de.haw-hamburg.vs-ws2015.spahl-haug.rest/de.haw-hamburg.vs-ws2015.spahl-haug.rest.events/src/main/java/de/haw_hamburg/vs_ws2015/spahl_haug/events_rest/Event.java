package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private int id;
    private String type; // Event Type e.g. bank transfer, rent, got to jail, estate transfer
    private String name;
    private String reason;
    private String resource;
    @JsonIgnore
    private List<String> uris;

    public Event(String type, String name, String reason, int id) {
        this.type = type;
        this.name = name;
        this.reason = reason;
        this.id = id;
        this.resource = "/events/" + id;
        this.uris = new ArrayList<>();
    }

    public int getId() {
        return id;
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

    @JsonIgnore
    public List<String> getUris() {
        return uris;
    }

    public void addUri(String uri) {
        uris.add(uri);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                ", resource='" + resource + '\'' +
                '}';
    }
}