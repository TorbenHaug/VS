package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BoardServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexAllreadyAquiredException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexIsYoursException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.GamesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.PlayersDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;


@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static GameService gameService;
	private static String serviceID = null;

	public static void setServiceID(final String id){
		serviceID = id;
	}

	@RequestMapping(value = "/api", method = RequestMethod.GET,  produces = "application/json")
	public String getAPI() {
		final String api = "This is our api";
		return api;
	}

	@RequestMapping(value = "/games", method = RequestMethod.POST,  produces = "application/json")
	public ResponseEntity<Game> createGame() throws BoardServiceNotFoundException {
		final Game game = gameService.createNewGame();
		return new ResponseEntity<>(game , HttpStatus.CREATED);
	}

	@RequestMapping(value = "/games", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<GamesDTO> getGames() {
		final GamesDTO games = new GamesDTO();
		games.setGames(gameService.getAllGames());
		return new ResponseEntity<>(games , HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Player> getPlayerFromGame(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID) throws GameDoesntExistsException, PlayerDoesntExistsException {
		final Player player = gameService.getPlayerFromGame(gameID, playerID);
		return new ResponseEntity<>(player, HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Game> getGame(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException {
		return new ResponseEntity<>(gameService.getGame(gameID) , HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}", method = RequestMethod.DELETE,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void deleteGame(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException {
		gameService.deleteGame(gameID);
	}

	@RequestMapping(value = "/games/{gameID}/players", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<PlayersDTO> getPlayersFromGame(@PathVariable(value="gameID") final long gameID) throws GameDoesntExistsException {
		final PlayersDTO playersDTO = new PlayersDTO();
		playersDTO.setPlayers(gameService.getplayersFromGame(gameID));
		return new ResponseEntity<>(playersDTO , HttpStatus.OK);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}", method = RequestMethod.PUT,  produces = "application/json")
	public void addPlayerToGame(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID, @RequestParam("playerName") final String playerName, @RequestParam("playerURI") final String playerURI) throws GameDoesntExistsException, BoardServiceNotFoundException{
		gameService.addPlayerToGame(gameID, playerID, playerName, playerURI);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}", method = RequestMethod.DELETE,  produces = "application/json")
	public void removePlayerFromGame(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID) throws GameDoesntExistsException{
		gameService.removePlayerFromGame(gameID, playerID);
	}

	@RequestMapping(value = "/games/{gameID}/players/{playerID}/ready", method = RequestMethod.PUT,  produces = "application/json")
	public void signalPlayerReady(@PathVariable(value="gameID") final long gameID, @PathVariable(value="playerID") final String playerID) throws GameDoesntExistsException, PlayerDoesntExistsException{
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
		//		final ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
		//final IServiceRepository serviceRepository = new ServiceRepository();
		final IServiceRepository serviceRepository = new IServiceRepository() {

			@Override
			public String getService(final String name) throws Exception {
				return "http://192.168.99.100:4569/boards";
			}
		};
		gameService = new GameService(serviceRepository);
		SpringApplication.run(Main.class, args);
	}

	public static String getServiceID() {
		return serviceID;
	}

}
