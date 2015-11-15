package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.Game;

public class GamesDTO {
	private List<Game> games;

	public List<Game> getGames() {
		return games;
	}

	public void setGames(final List<Game> games) {
		this.games = games;
	}
}
