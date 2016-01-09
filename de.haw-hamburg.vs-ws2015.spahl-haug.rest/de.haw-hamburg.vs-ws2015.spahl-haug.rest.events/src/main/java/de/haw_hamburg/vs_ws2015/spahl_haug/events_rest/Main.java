package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;


import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameHasNoEventsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.EventOutDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.EventsOutDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.SubscriptionDTO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static String serviceId;
	private final EventService eventService = new EventService();
	private static RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	// Example = http://<ip address>:<port>/events?gameid=<variable>
	public ResponseEntity<EventsOutDTO> getEvents(@RequestParam("gameid") final String gameid) throws GameHasNoEventsException {
		final List<String> events = eventService.getEvents(gameid);
		return new ResponseEntity<>(new EventsOutDTO(events), HttpStatus.OK);
	}

	@RequestMapping(value = "/events", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	// Example = http://<ip address>:<port>/events?gameid=<variable>
	public ResponseEntity<String> createEvent(@RequestParam("gameid") final String gameid, @RequestBody final EventDTO event) throws Exception {
		final String eventUri = eventService.createEvent(gameid, event.getType(), event.getName(), event.getReason(), event.getResource(), event.getPlayer());
		final Set<String> subscriberList = eventService.getSubscriber(gameid, event.getType());
		System.out.println("Announce for Game " + gameid + ": " + event);
		new Thread(){
			@Override
			public void run() {
				for(final String subscriber : subscriberList) {
					//            EventOutDTO eventOutDTO = new EventOutDTO(event.getType(), event.getName(), event.getReason(), eventUri, event.getPlayer());
					System.out.println("Announcing: " + subscriber);
					try{
						restTemplate.postForEntity(subscriber, eventUri, String.class);
					}catch(final RestClientException e){
						System.out.println("Cant find " + subscriber);
						eventService.removeSubscribtion(gameid, subscriber, event.getType());
					}
				}
			};
		}.start();

		return new ResponseEntity<>(eventUri, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/events", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	// Example = http://<ip address>:<port>/events?gameid=<variable>
	public void removeEvent(@RequestParam("gameid") final String gameid){
		eventService.removeEvents(gameid);
	}


	@RequestMapping(value = "/events/{eventid}", method = RequestMethod.GET, produces = "application/json")
	public Event getEvent(@PathVariable(value = "eventid") final int eventid) throws EventNotFoundException {
		return eventService.getEvent(eventid);
	}

	@RequestMapping(value = "/events/subscriptions", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void createSubscribtion(@RequestBody final SubscriptionDTO subscription) throws Exception {
		final String gameId = subscription.getGameid();
		final String uri = subscription.getUri();
		final String eventType = subscription.getEvent().getType();
		eventService.subscribe(gameId, uri, eventType);
	}

	@RequestMapping(value = "/events/subscriptions", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void removeSubscribtion(@RequestBody final SubscriptionDTO subscription) throws Exception {
		final String gameId = subscription.getGameid();
		final String uri = subscription.getUri();
		final String eventType = subscription.getEvent().getType();
		eventService.removeSubscribtion(gameId, uri, eventType);
	}

	@RequestMapping(value = "/events/subscriptions", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	public void getSubscribtions() {

	}


	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}


	public static void setServiceID(final String serviceId) {
		System.out.println("Registred as " + serviceId);
		Main.serviceId = serviceId;

	}

	public static String getServiceID() {
		return Main.serviceId;
	}


}
