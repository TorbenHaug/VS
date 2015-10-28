package de.haw_hamburg.vs_ws2015.spahl_haug.dice_rest;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

    @RequestMapping(value = "/dice", method = RequestMethod.GET,  produces = "application/json")
    public Roll dice() {
    	System.out.println("Anfrage");
        return new Roll();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
