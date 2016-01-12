package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;

import java.util.ArrayList;
import java.util.List;

public class PlayersDTO {
	private List<String> players = new ArrayList<String>();

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(final List<String> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return "PlayersDTO [players=" + players + "]";
	}


}
