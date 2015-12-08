package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;


import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameHasNoEventsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.EventOutDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.SubscriptionDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

    private static String serviceId;
    private EventService eventService = new EventService();
    private static RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/events", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    // Example = http://<ip address>:<port>/events?gameid=<variable>
    public ResponseEntity<TreeMap<Integer, Event>> getEvents(@RequestParam("gameid") String gameid) throws GameHasNoEventsException {
        TreeMap<Integer, Event> events = eventService.getEvents(gameid);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    // Example = http://<ip address>:<port>/events?gameid=<variable>
    public ResponseEntity<String> createEvent(@RequestParam("gameid") String gameid, @RequestBody EventDTO event) throws Exception {
        String eventUri = eventService.createEvent(gameid, event.getType(), event.getName(), event.getReason(), event.getPlayer());
        List<String> subscriberList = eventService.getSubscriber(gameid, event.getType());
        for(String subscriber : subscriberList) {
            EventOutDTO eventOutDTO = new EventOutDTO(event.getType(), event.getName(), event.getReason(), eventUri, event.getPlayer());
            restTemplate.postForEntity(subscriber, eventOutDTO, EventOutDTO.class);
        }
        return new ResponseEntity<>(eventUri, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/events", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    // Example = http://<ip address>:<port>/events?gameid=<variable>
    public void removeEvent(@RequestParam("gameid") String gameid){
        eventService.removeEvents(gameid);
    }


    @RequestMapping(value = "/events/{eventid}", method = RequestMethod.GET, produces = "application/json")
    public Event getEvent(@RequestParam("gameid") String gameid, @PathVariable(value = "eventid") int eventid) {
        return eventService.getEvent(gameid, eventid);
    }

    @RequestMapping(value = "/events/subscriptions", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void createSubscribtion(@RequestBody SubscriptionDTO subscription) throws Exception {
        String gameId = subscription.getGameid();
        String uri = subscription.getUri();
        String eventType = subscription.getEvent().getType();
        eventService.subscribe(gameId, uri, eventType);
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
