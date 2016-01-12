package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto;

import java.util.ArrayList;
import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.Player;

public class PlayersDTO {
	private final List<String> players = new ArrayList<>();

	public PlayersDTO(final List<Player> players) {
		for(final Player player: players){
			this.players.add(player.getUri());
		}
	}

	public List<String> getPlayers() {
		return players;
	}
}
