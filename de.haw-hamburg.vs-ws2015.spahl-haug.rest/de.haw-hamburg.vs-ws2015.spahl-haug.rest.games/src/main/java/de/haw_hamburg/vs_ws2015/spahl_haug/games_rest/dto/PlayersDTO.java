package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.Player;

public class PlayersDTO {
	private List<Player> players;

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(final List<Player> players) {
		this.players = players;
	}
}
