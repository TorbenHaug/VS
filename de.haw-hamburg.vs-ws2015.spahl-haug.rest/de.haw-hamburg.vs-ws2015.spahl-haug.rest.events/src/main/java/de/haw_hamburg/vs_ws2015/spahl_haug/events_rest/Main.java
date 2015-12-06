package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;


import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameHasNoEventsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.events_rest.dto.EventDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.TreeMap;

@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

    private EventService eventService = new EventService();

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
     public ResponseEntity<String> createEvent(@RequestParam("gameid") String gameid, @RequestBody EventDTO event){
        String eventUri = eventService.createEvent(gameid, event.getType(), event.getName(), event.getReason());
        return new ResponseEntity<>(eventUri, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/events", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    // Example = http://<ip address>:<port>/events?gameid=<variable>
    public void removeEvent(@RequestParam("gameid") String gameid){
        eventService.removeEvents(gameid);
    }


    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }


}

