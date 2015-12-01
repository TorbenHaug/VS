package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GameDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PositionNoOnBoardException;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {
	private static BoardService boardService = new BoardService();
	private final static String urlString = "http://192.168.99.100:4568";
	private static RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/boards", method = RequestMethod.GET,  produces = "application/json")
	public GamesDTO getGames() {
		final GamesDTO gamesDTO = restTemplate.getForObject(urlString + "/games", GamesDTO.class);
		return gamesDTO;
	}

    @RequestMapping(value = " /boards/{gameid}", method = RequestMethod.GET,  produces = "application/json")
    public ResponseEntity<Board> getBoardToGame(@PathVariable(value="gameid") final long gameID) throws GameDoesntExistsException {
        GamesDTO gamesDTO = restTemplate.getForObject(urlString + "/games", GamesDTO.class);
        if (isGameIdValid(gameID)) {
            final Board board = boardService.getBoard(gameID);
            return new ResponseEntity<>(board, HttpStatus.OK);
        } else {
            throw new GameDoesntExistsException("Game does not Exists");
        }
    }

    @RequestMapping(value = " /boards/{gameid}", method = RequestMethod.PUT,  produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBoard(@PathVariable(value="gameid") final long gameID) throws GameDoesntExistsException {
        if (isGameIdValid(gameID)) {
            boardService.createBoard(gameID);
        } else {
            throw new GameDoesntExistsException("Game does not Exists");
        }
    }

    @RequestMapping(value = " /boards/{gameid}", method = RequestMethod.DELETE,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBoard(@PathVariable(value="gameid") final long gameID) throws GameDoesntExistsException {
        if (isGameIdValid(gameID)) {
            String url =  urlString + "games/" + gameID ;
            restTemplate.delete(url);
            boardService.deleteBoard(gameID);
        } else {
            throw new GameDoesntExistsException("Game does not Exists");
        }
    }

//    @RequestMapping(value = " /boards/{gameid}/players", method = RequestMethod.GET,  produces = "application/json")
//    @ResponseStatus(HttpStatus.OK)
//    public void getPlayersPositionFromBoard(@PathVariable(value="gameid") final long gameID) throws GameDoesntExistsException {
//        if (isGameIdValid(gameID)) {
//            boardService.deleteBoard(gameID);
//        } else {
//            throw new GameDoesntExistsException("Game does not Exists");
//        }
//    }

    @RequestMapping(value = " /boards/{gameid}/players/{playerid}", method = RequestMethod.PUT,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void  placePlayer(@PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") String playerID) throws GameDoesntExistsException, PositionNoOnBoardException, PlayerDoesntExistsException {
        if (isGameIdValid(gameID)) {
            boardService.placePlayer(gameID, playerID);
        } else {
            throw new GameDoesntExistsException("Game does not Exists");
        }
    }

    @RequestMapping(value = " /boards/{gameid}/players/{playerid}", method = RequestMethod.DELETE,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void removePlayerFromBoard(@PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") String playerID) throws GameDoesntExistsException {
        if (isGameIdValid(gameID)) {
            boardService.removePlayerFromBoard(gameID, playerID);
        } else {
            throw new GameDoesntExistsException("Game does not Exists");
        }
    }


    @RequestMapping(value = " /boards/{gameid}/players/{playerid}", method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void getPlayerPostion(@PathVariable(value="gameid") final long gameID, @PathVariable(value="playerid") String playerID) throws GameDoesntExistsException, PlayerDoesntExistsException {
        if (isGameIdValid(gameID)) {
            boardService.getPlayerPosition(gameID, playerID);
        } else {
            throw new GameDoesntExistsException("Game does not Exists");
        }
    }


    @RequestMapping(value = " /boards/{gameid}/places", method = RequestMethod.GET,  produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<Place>  getAvailablePlacesOnBoard(@PathVariable(value="gameid") final long gameID) throws GameDoesntExistsException {
        if (isGameIdValid(gameID)) {
            return boardService.getPlaces(gameID);
        } else {
            throw new GameDoesntExistsException("Game does not Exists");
        }
    }


    /**
     * Validate if gameID is a valid id of any game
     * @param gameID Id of a game
     * @return true if gameID is a valid id, false otherwise
     */
    private boolean isGameIdValid(long gameID) {
        GamesDTO gamesDTO = restTemplate.getForObject(urlString + "/games", GamesDTO.class);
        // check if gameid exists
        for(GameDTO game : gamesDTO.getGames()){
            if(gameID == game.getGameid()) {
                return true;
            }
        }
        return false;
    }


    public static void main(final String[] args) throws Exception {
		//		final ServiceRepository repo = new ServiceRepository();
		//		System.err.println(repo.getService("spahl_haug_games"));
		SpringApplication.run(Main.class, args);
	}


}
