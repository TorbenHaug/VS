package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameHasNoEventsException;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class EventService {
    private Map<String, TreeMap<Integer, Event>> events;

    public EventService() {
        this.events = new HashMap<>();
    }

    public Event getEvent(String gameId, int eventId) {
        return events.get(gameId).get(eventId);
    }

    public TreeMap<Integer, Event> getEvents(String gameId) throws GameHasNoEventsException {
        TreeMap<Integer, Event> tmp = events.get(gameId);
        if(tmp == null) {
            throw new GameHasNoEventsException("There are no events for the specified game");
        }
        return events.get(gameId);
    }

    public String createEvent(String gameId, String type, String name, String reason){
        if(events.containsKey(gameId)) {
            TreeMap<Integer, Event> eventMap = events.get(gameId);
            int highestId = eventMap.lastKey();
            int newId = highestId + 1;
            Event event = new Event(type, name, reason, newId);
            eventMap.put(newId, event);
            return event.getResource();
        } else {
            TreeMap<Integer, Event> newEventMap = new TreeMap<>();
            Event event = new Event(type, name, reason, 1);
            newEventMap.put(1, event);
            events.put(gameId, newEventMap);
            return event.getResource();
        }
    }

    public void removeEvents(String gameId) {
        events.remove(gameId);
    }

    public List<String> subscribe(String gameId, String uri, String eventType) throws Exception {
        for(Map.Entry<Integer, Event> entry : events.get(gameId).entrySet()) {
            Event event = entry.getValue();
            if(event.getType().equals(eventType)) {
                event.addUri(uri);
            }
        }

        for(Map.Entry<Integer, Event> entry : events.get(gameId).entrySet()) {
            Event event = entry.getValue();
            if (event.getType().equals(eventType)) {
                return event.getUris();
            }
        }
        throw new Exception();
    }
}
