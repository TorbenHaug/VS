package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameHasNoEventsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EventService {
    // Map<gameId, TrreMap<eventId, Event>>
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

    public String createEvent(String gameId, String type, String name, String reason, String resource, String player){
        if(events.containsKey(gameId)) {
            TreeMap<Integer, Event> eventMap = events.get(gameId);
            int highestId = eventMap.lastKey();
            int newId = highestId + 1;
            Event event = new Event(type, name, reason, resource, newId, player);
            eventMap.put(newId, event);
            return event.getEventUri();
        } else {
            TreeMap<Integer, Event> newEventMap = new TreeMap<>();
            Event event = new Event(type, name, reason, resource, 1, player);
            newEventMap.put(1, event);
            events.put(gameId, newEventMap);
            return event.getEventUri();
        }
    }

    public void removeEvents(String gameId) {
        events.remove(gameId);
    }

    public void subscribe(String gameId, String uri, String eventType) throws Exception {
        for(Map.Entry<Integer, Event> entry : events.get(gameId).entrySet()) {
            Event event = entry.getValue();
            if(event.getType().equals(eventType)) {
                event.addUri(uri);
            }
        }
    }

    public List<String> getSubscriber(String gameId, String eventType) throws Exception {
        for(Map.Entry<Integer, Event> entry : events.get(gameId).entrySet()) {
            Event event = entry.getValue();
            if (event.getType().equals(eventType)) {
                return event.getUris();
            }
        }
        throw new Exception();
    }

}
