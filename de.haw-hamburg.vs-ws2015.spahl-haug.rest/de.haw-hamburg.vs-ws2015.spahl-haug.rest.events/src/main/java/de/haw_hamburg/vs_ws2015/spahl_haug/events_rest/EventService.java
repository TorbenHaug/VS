package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameHasNoEventsException;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EventService {
    private Map<String, TreeMap<Integer, Event>> events;

    public EventService() {
        this.events = new HashMap<>();
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

    public void removeEvents(String gameid) {
        events.remove(gameid);
    }
}
