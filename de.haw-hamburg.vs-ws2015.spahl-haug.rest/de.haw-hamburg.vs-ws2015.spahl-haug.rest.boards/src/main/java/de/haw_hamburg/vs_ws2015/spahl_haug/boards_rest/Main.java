package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;
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
	public GamesDTO createGame() {
		RestTemplate restTemplate = new RestTemplate();
		GamesDTO gamesDTO = restTemplate.getForObject(urlString + "/games", GamesDTO.class);
		return gamesDTO;
	}

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

}
