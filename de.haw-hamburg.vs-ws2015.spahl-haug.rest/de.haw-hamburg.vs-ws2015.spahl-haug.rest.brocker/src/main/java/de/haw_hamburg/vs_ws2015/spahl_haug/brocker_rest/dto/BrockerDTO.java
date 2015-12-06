package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto;

import java.util.ArrayList;
import java.util.List;

public class BrockerDTO {
	private String gameid;
	private String uri;
	private List<Player> players;

	public BrockerDTO() {
		players = new ArrayList<>();
	}

	public String getGameid() {
		return gameid;
	}

	public void setGameid(final String gameid) {
		this.gameid = gameid;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(final String uri) {
		this.uri = uri;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(final List<Player> players) {
		this.players = players;
	}


}
