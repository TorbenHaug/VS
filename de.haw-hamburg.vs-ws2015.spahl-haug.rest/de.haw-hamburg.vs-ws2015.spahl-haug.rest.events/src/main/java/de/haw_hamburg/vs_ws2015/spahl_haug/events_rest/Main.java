package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

    @RequestMapping(value = "/events", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String getEvents(){
        return "NOT AVAILABLE YET";
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    // Example = http://<ip address>:<port>/events?gameid=<variable>
    public String createEvent(@RequestParam("gameid") String gameid){
        return gameid;
    }

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }


}

