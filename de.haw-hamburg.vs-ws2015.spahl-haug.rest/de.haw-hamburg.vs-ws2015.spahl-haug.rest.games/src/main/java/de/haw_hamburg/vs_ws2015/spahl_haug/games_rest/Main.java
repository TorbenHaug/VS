package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.List;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.GamesDTO;


@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static GameService gameService = new GameService();

	@RequestMapping(value = "/games", method = RequestMethod.POST,  produces = "application/json")
	public ResponseEntity<Game> createGame() {
		final Game game = gameService.createNewGame();
		return new ResponseEntity<>(game , HttpStatus.CREATED);
	}

	@RequestMapping(value = "/games", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<GamesDTO> getGames() {
		final GamesDTO games = new GamesDTO();
		games.setGames(gameService.getAllGames());
		return new ResponseEntity<>(games , HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}", method = RequestMethod.PUT,  produces = "application/json")
	public void addPlayerToGame(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final long playerID) throws GameDoesntExistsException{
		gameService.addPlayerToGame(gameID, playerID);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}/ready", method = RequestMethod.PUT,  produces = "application/json")
	public void signalPlayerReady(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final long playerID) throws GameDoesntExistsException, PlayerDoesntExistsException{
		gameService.signalPlayerReady(gameID, playerID);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}/ready", method = RequestMethod.GET,  produces = "application/json")
	public boolean getPlayerReady(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final long playerID) throws GameDoesntExistsException, PlayerDoesntExistsException{
		return gameService.getPlayerReady(gameID, playerID);
	}

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

}
