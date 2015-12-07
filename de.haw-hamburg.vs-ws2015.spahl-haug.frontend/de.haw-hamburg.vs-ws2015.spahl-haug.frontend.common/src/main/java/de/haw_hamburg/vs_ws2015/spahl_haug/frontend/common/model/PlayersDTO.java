package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;

import java.util.ArrayList;
import java.util.List;

public class PlayersDTO {
	private List<Player> players = new ArrayList<Player>();

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(final List<Player> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return "PlayersDTO [players=" + players + "]";
	}


}
