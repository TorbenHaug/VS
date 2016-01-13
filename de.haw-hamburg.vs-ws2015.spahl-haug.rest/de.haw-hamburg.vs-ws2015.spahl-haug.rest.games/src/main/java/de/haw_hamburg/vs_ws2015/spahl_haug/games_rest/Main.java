package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;

import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BoardServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameFullException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexAllreadyAquiredException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexIsYoursException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.GameDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.GamesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.PlayersDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;


@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static GameService gameService;
	private static String serviceID = null;
	static String uri;

	public static void setServiceID(final String id){
		serviceID = id;
	}

	@RequestMapping(value = "/api", method = RequestMethod.GET,  produces = "application/json")
	public String getAPI() {
		final String api = "This is our api";
		return api;
	}

	@RequestMapping(value = "/games", method = RequestMethod.POST,  produces = "application/json")
	public ResponseEntity<GameDTO> createGame(@RequestBody final Components components) throws BoardServiceNotFoundException {
		final Game game = gameService.createNewGame(components);
		final GameDTO gameDTO = new GameDTO(game.getGameid(), game.getUri() + "/players",game.getUri(), game.isStarted(), game.getComponents());
		return new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/games", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<GamesDTO> getGames() {
		final GamesDTO games = new GamesDTO(gameService.getAllGames());
		return new ResponseEntity<>(games , HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Player> getPlayerFromGame(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID) throws GameDoesntExistsException, PlayerDoesntExistsException {
		final Player player = gameService.getPlayerFromGame(gameID, playerID);
		return new ResponseEntity<>(player, HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<GameDTO> getGame(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException {
		final Game game = gameService.getGame(gameID);
		final GameDTO gameDTO = new GameDTO(game.getGameid(), game.getUri() + "/players",game.getUri(), game.isStarted(), game.getComponents());
		return new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/games/{gameID}", method = RequestMethod.DELETE,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void deleteGame(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException {
		gameService.deleteGame(gameID);
	}

	@RequestMapping(value = "/games/{gameID}/players", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<PlayersDTO> getPlayersFromGame(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException {
		final PlayersDTO playersDTO = new PlayersDTO(gameService.getplayersFromGame(gameID));
		return new ResponseEntity<>(playersDTO , HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}", method = RequestMethod.PUT,  produces = "application/json")
	public void addPlayerToGame(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID, @RequestParam(value="name") final String playerName, @RequestParam(value="uri") final String playerURI, final UriComponentsBuilder uriBuilder, final HttpServletResponse response) throws GameDoesntExistsException, BoardServiceNotFoundException, GameFullException{
		gameService.addPlayerToGame(gameID, playerID, playerName, playerURI);
		response.setHeader("Location", uriBuilder.path("/games/{gameID}/players/{playerID}").buildAndExpand(gameID, playerID).toUriString());
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}", method = RequestMethod.DELETE,  produces = "application/json")
	public void removePlayerFromGame(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID) throws GameDoesntExistsException{
		gameService.removePlayerFromGame(gameID, playerID);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}/ready", method = RequestMethod.PUT,  produces = "application/json")
	public void signalPlayerReady(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID) throws GameDoesntExistsException, PlayerDoesntExistsException, GameNotStartedException, BoardServiceNotFoundException{
		gameService.signalPlayerReady(gameID, playerID);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}/ready", method = RequestMethod.GET,  produces = "application/json")
	public boolean getPlayerReady(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID) throws GameDoesntExistsException, PlayerDoesntExistsException{
		return gameService.getPlayerReady(gameID, playerID);
	}

	@RequestMapping(value = "/games/{gameID}/players/current", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Player> getCurrentPlayer(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException, GameNotStartedException {
		final Player player = gameService.getCurrentPlayer(gameID);
		return new ResponseEntity<>(player, HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}/players/turn", method = RequestMethod.GET,  produces = "application/json")
	public Player getPlayerTurn(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException{
		return gameService.getMutex(gameID);
	}

	@RequestMapping(value = "/games/{gameID}/players/turn", method = RequestMethod.PUT,  produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void putPlayerTurn(@PathVariable(value="gameID") final long gameID, @RequestParam("player") final String playerId) throws GameDoesntExistsException, MutexAllreadyAquiredException, MutexIsYoursException, PlayerDoesntExistsException{
		gameService.aquireMutex(gameID,playerId);
	}

	@RequestMapping(value = "/games/{gameID}/players/turn", method = RequestMethod.DELETE,  produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void deletePlayerTurn(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException{
		gameService.releaseMutex(gameID);
	}

	public static void main(final String[] args) throws Exception {
		gameService = new GameService();
		SpringApplication.run(Main.class, args);
	}

	public static String getServiceID() {
		return serviceID;
	}

	public static void setOwnURI(final String uri) {
		Main.uri = uri;

	}

}
