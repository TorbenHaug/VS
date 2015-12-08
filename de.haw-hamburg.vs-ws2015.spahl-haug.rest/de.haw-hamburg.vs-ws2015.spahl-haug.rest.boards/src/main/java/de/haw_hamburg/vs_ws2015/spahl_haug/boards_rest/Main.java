package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.ArrayList;
import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.*;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.*;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
	private static BoardService boardService = new BoardService();
	private static RestTemplate restTemplate = new RestTemplate();

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
	public void createBoard(@PathVariable(value="gameid") final long gameID) {
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
	public void  placePlayer(@PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") final String playerID) throws PositionNotOnBoardException, PlayerDoesntExistsException {
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
        Player p = boardService.getPlayerPosition(gameID, playerID);
        PlayerDTO player = new PlayerDTO(playerID, gameID, p.getPosition());
        return player;
	}


	@RequestMapping(value = " /boards/{gameid}/places", method = RequestMethod.GET,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<PlaceDTO>  getAvailablePlacesOnBoard(@PathVariable(value="gameid") final long gameID)  {
        List<PlaceDTO> placeList = new ArrayList<>();
        for(Place place : boardService.getPlaces(gameID)) {
            PlaceDTO placeDTO = new PlaceDTO(place.toString());
            placeList.add(placeDTO);
        }
		return placeList;
	}


	@RequestMapping(value = " /boards/{gameid}/players/{playerid}/roll", method = RequestMethod.POST,  produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BoardsServiceDTO> postRoll(@RequestBody RollsDTO roll, @PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") final String playerID) throws PlayerDoesntExistsException, RollnumberNotAcceptableException, PositionNotOnBoardException {
        int roll1 = roll.getRoll1().getNumber();
        int roll2 = roll.getRoll2().getNumber();
        if((roll1 < 1 || roll1 > 6) || (roll2 < 1 || roll2 > 6)) {
            throw new RollnumberNotAcceptableException("The Roll numbers are not in the range 1 to 6");
        }
        int rollSum = roll1 + roll2;
        Board board = boardService.placePlayer(gameID, playerID, rollSum);

        Player player = board.getPlayer(playerID);
        List<Field> fields = board.getFields();
        List<FieldDTO> f = new ArrayList<>();
        for(Field field : fields) {
            List<PlayerDTO> playerList = new ArrayList<>();
            for(Player player1 : field.getPlayers()){
                PlayerDTO playerDTO = new PlayerDTO(player1.getId(), gameID, player1.getPosition());
                playerList.add(playerDTO);
            }
            FieldDTO fieldDTO = new FieldDTO(gameID, field.getPlace().getPosition(), playerList);
            f.add(fieldDTO);
        }
        BoardDTO boardDTO = new BoardDTO(f);
        PlayerDTO playerDTO = new PlayerDTO(player.getId(), gameID, player.getPosition());
        BoardsServiceDTO boardsServiceDTO = new BoardsServiceDTO(playerDTO, boardDTO);

        return new ResponseEntity<>(boardsServiceDTO, HttpStatus.OK);
	}


	public static void main(final String[] args) throws Exception {
		//		final ServiceRepository repo = new ServiceRepository();
		//		System.err.println(repo.getService("spahl_haug_games"));
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
