package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.ArrayList;
import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.*;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.*;

import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.net.ssl.SSLEngineResult;


@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static String serviceId;
	private static BoardService boardService;
	//	private static RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/boards", method = RequestMethod.GET,  produces = "application/json")
	public BoardsDTO getGames() {
		return new BoardsDTO(new ArrayList<>(boardService.getBoards().values()));
	}

	@RequestMapping(value = " /boards/{gameid}", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Board> getBoardToGame(@PathVariable(value="gameid") final long gameID) {
		final Board board = boardService.getBoard(gameID);
		return new ResponseEntity<>(board, HttpStatus.OK);
	}

	// von Game aufgerufen
	@RequestMapping(value = " /boards/{gameid}", method = RequestMethod.PUT,  produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createBoard(@PathVariable(value="gameid") final long gameID) throws BankServiceNotFoundException, BrokerServiceNotFoundException {
		boardService.createBoard(gameID);
	}

	@RequestMapping(value = " /boards/{gameid}", method = RequestMethod.DELETE,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void deleteBoard(@PathVariable(value="gameid") final long gameID) {
		boardService.deleteBoard(gameID);
	}

	@RequestMapping(value = " /boards/{gameid}/players", method = RequestMethod.GET,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<Player> getPlayersPositionFromBoard(@PathVariable(value="gameid") final long gameID) {
		return boardService.getPlayerFromBoard(gameID);
	}

	// von Game aufgerufen
	@RequestMapping(value = " /boards/{gameid}/players/{playerid}", method = RequestMethod.PUT,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void  placePlayer(@PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") final String playerID) throws PositionNotOnBoardException, PlayerDoesntExistsException, RestClientException, BankServiceNotFoundException {
		boardService.placePlayer(gameID, playerID);
	}

	@RequestMapping(value = " /boards/{gameid}/players/{playerid}", method = RequestMethod.DELETE,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void removePlayerFromBoard(@PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") final String playerID) {
		boardService.removePlayerFromBoard(gameID, playerID);
	}


	@RequestMapping(value = " /boards/{gameid}/players/{playerid}", method = RequestMethod.GET,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public PlayerDTO getPlayerPostion(@PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") final String playerID) throws PlayerDoesntExistsException {
		final Player p = boardService.getPlayerPosition(gameID, playerID);
		final PlayerDTO player = new PlayerDTO(playerID, gameID, p.getPosition());
		return player;
	}


	@RequestMapping(value = " /boards/{gameid}/places", method = RequestMethod.GET,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<PlaceDTO>  getAvailablePlacesOnBoard(@PathVariable(value="gameid") final long gameID)  {
		final List<PlaceDTO> placeList = new ArrayList<>();
		for(final Place place : boardService.getPlaces(gameID)) {
			final PlaceDTO placeDTO = new PlaceDTO(place.toString());
			placeList.add(placeDTO);
		}
		return placeList;
	}


	@RequestMapping(value = " /boards/{gameid}/players/{playerid}/roll", method = RequestMethod.POST,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BoardsServiceDTO> postRoll(@RequestBody final RollsDTO roll, @PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") final String playerID) throws PlayerDoesntExistsException, RollnumberNotAcceptableException, PositionNotOnBoardException, GameDoesntExistsException {
		return new ResponseEntity<BoardsServiceDTO>(boardService.movePlayer(roll,gameID,playerID), HttpStatus.OK);
	}


	public static void main(final String[] args) throws Exception {
		//		final ServiceRepository repo = new ServiceRepository();
		//		System.err.println(repo.getService("spahl_haug_games"));
		boardService = new BoardService(new ServiceRepository());
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
