package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameHasNoEventsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.events.EventException;

public class EventService {
	//GameID,EventTyp,ResponseURIs
	Map<String,Map<String,Set<String>>> subscribers = new ConcurrentHashMap<>();
	Map<Integer,Event> events = new ConcurrentHashMap<>();
	Map<String, List<Integer>> gameEventIds = new ConcurrentHashMap<>();
	private int nextNr = 0;

	public void subscribe(final String gameId, final String uri, final String eventType) {
		final Set<String> eventSubscribers = getEventSubscribers(gameId,eventType);
		eventSubscribers.add(uri);

	}

	private Set<String> getEventSubscribers(final String gameId, final String eventType) {
		final Map<String,Set<String>> gamesubscribers = getSubscribers(gameId);
		Set<String> eventSubscribers = gamesubscribers.get(eventType);
		if(eventSubscribers == null){
			eventSubscribers = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>());
			gamesubscribers.put(eventType, eventSubscribers);
		}
		return eventSubscribers;
	}

	private Map<String, Set<String>> getSubscribers(final String gameId) {
		Map<String,Set<String>> gamesubscribers = subscribers.get(gameId);
		if(gamesubscribers == null){
			gamesubscribers = new ConcurrentHashMap<>();
			subscribers.put(gameId, gamesubscribers);
		}
		return gamesubscribers;
	}

	public Set<String> getSubscriber(final String gameid, final String type) {
		return getEventSubscribers(gameid, type);
	}

	public String createEvent(final String gameid, final String type, final String name, final String reason, final String resource, final String player) {
		final Event event = new Event(type, name, reason, resource, getUniqueNr(), player);
		events.put(event.getId(), event);
		final List<Integer> eventIdsforGame = getEventIdsforGame(gameid);
		eventIdsforGame.add(event.getId());
		return event.getUri();
	}

	private List<Integer> getEventIdsforGame(final String gameid) {
		List<Integer> eventIdsforGame = gameEventIds.get(gameid);
		if(eventIdsforGame == null){
			eventIdsforGame = Collections.synchronizedList(new ArrayList<>());
			gameEventIds.put(gameid, eventIdsforGame);
		}
		return eventIdsforGame;
	}

	synchronized private int getUniqueNr() {
		return nextNr++;
	}

	public void removeEvents(final String gameid) {
		final List<Integer> eventIdsforGame = getEventIdsforGame(gameid);
		for(final int eventId: eventIdsforGame){
			events.remove(eventId);
		}
	}

	public Event getEvent(final int eventid) throws EventNotFoundException {
		final Event event = events.get(eventid);
		if(event == null){
			throw new EventNotFoundException(eventid + " not found");
		}
		return event;
	}

	public List<String> getEvents(final String gameid) {
		final List<String> retVal = new ArrayList<>();
		for(final int eventId: getEventIdsforGame(gameid)){
			try {
				retVal.add(getEvent(eventId).getUri());
			} catch (final EventNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	public void removeSubscribtion(final String gameId, final String uri, final String eventType) {
		getSubscriber(gameId, eventType).remove(uri);
	}

	// Map<gameId, TrreMap<eventId, Event>>
	//    private Map<String, TreeMap<Integer, Event>> events;
	//
	//    public EventService() {
	//        this.events = new HashMap<>();
	//    }
	//
	//    public Event getEvent(String gameId, int eventId) {
	//        return events.get(gameId).get(eventId);
	//    }
	//
	//    public TreeMap<Integer, Event> getEvents(String gameId) throws GameHasNoEventsException {
	//        TreeMap<Integer, Event> tmp = events.get(gameId);
	//        if(tmp == null) {
	//            throw new GameHasNoEventsException("There are no events for the specified game");
	//        }
	//        return events.get(gameId);
	//    }
	//
	//    public String createEvent(String gameId, String type, String name, String reason, String resource, String player){
	//        if(events.containsKey(gameId)) {
	//            TreeMap<Integer, Event> eventMap = events.get(gameId);
	//            int highestId = eventMap.lastKey();
	//            int newId = highestId + 1;
	//            Event event = new Event(type, name, reason, resource, newId, player);
	//            eventMap.put(newId, event);
	//            return event.getEventUri();
	//        } else {
	//            TreeMap<Integer, Event> newEventMap = new TreeMap<>();
	//            Event event = new Event(type, name, reason, resource, 1, player);
	//            newEventMap.put(1, event);
	//            events.put(gameId, newEventMap);
	//            return event.getEventUri();
	//        }
	//    }
	//
	//    public void removeEvents(String gameId) {
	//        events.remove(gameId);
	//    }
	//
	//    public void subscribe(String gameId, String uri, String eventType) throws Exception {
	//        for(Map.Entry<Integer, Event> entry : events.get(gameId).entrySet()) {
	//            Event event = entry.getValue();
	//            if(event.getType().equals(eventType)) {
	//                event.addUri(uri);
	//            }
	//        }
	//    }
	//
	//    public List<String> getSubscriber(String gameId, String eventType) throws Exception {
	//        for(Map.Entry<Integer, Event> entry : events.get(gameId).entrySet()) {
	//            Event event = entry.getValue();
	//            if (event.getType().equals(eventType)) {
	//                return event.getUris();
	//            }
	//        }
	//        throw new Exception();
	//    }

}
