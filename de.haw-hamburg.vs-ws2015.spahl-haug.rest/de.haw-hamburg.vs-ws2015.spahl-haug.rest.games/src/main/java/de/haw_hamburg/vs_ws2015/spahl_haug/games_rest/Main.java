package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static GameService gameService = new GameService();
	
    @RequestMapping(value = "/games", method = RequestMethod.POST,  produces = "application/json")
    public ResponseEntity<Game> games() {
    	Game game = gameService.createNewGame();
    	System.out.println("Create new Game: " + game.getId());
        return new ResponseEntity<>(game , HttpStatus.CREATED);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
