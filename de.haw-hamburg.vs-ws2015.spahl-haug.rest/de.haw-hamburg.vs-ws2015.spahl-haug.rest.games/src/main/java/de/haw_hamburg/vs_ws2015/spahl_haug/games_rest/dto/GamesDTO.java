package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto;

import java.util.ArrayList;
import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;

public class GamesDTO {
	private final List<GameDTO> games = new ArrayList<>();

	public GamesDTO(final List<Game> games) {
		for(final Game game: games){
			this.games.add(new GameDTO(game.getGameid(), game.getUri() + "/players",game.getUri(), game.isStarted(), game.getComponents()));
		}
	}

	public List<GameDTO> getGames() {
		return games;
	}
}
