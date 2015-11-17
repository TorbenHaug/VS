package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;
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
	private final static String urlString = "http://141.22.71.60:4568";

	@RequestMapping(value = "/boards", method = RequestMethod.GET,  produces = "application/json")
	public GamesDTO getGames() {
		RestTemplate restTemplate = new RestTemplate();
		GamesDTO gamesDTO = restTemplate.getForObject(urlString + "/games", GamesDTO.class);
		return gamesDTO;
	}

    @RequestMapping(value = " /boards/{gameid}", method = RequestMethod.GET,  produces = "application/json")
    public Board getBoardToGame(@PathVariable(value="gameid") final long gameID) {
        return boardService.getBoard(gameID);
    }

    @RequestMapping(value = " /boards/{gameid}", method = RequestMethod.PUT,  produces = "application/json")
    public Board createBoard(@PathVariable(value="gameid") final long gameID) {
        return null;
    }

    public static void main(final String[] args) throws Exception {
		//		final ServiceRepository repo = new ServiceRepository();
		//		System.err.println(repo.getService("spahl_haug_games"));
		SpringApplication.run(Main.class, args);
	}

}
