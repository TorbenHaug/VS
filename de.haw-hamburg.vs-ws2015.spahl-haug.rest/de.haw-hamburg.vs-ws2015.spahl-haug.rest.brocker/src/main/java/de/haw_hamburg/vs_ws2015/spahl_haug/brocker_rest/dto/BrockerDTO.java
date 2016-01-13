package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto;

import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;

public class BrockerDTO {
	private String gameid;
	private String uri;
	private String players;
	private Components components;

	public BrockerDTO() {
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

	public String getPlayers() {
		return players;
	}

	public void setPlayers(final String players) {
		this.players = players;
	}

	public Components getComponents() {
		return components;
	}

	public void setComponents(final Components components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "BrockerDTO [gameid=" + gameid + ", uri=" + uri + ", players=" + players + ", components=" + components
				+ "]";
	}


}
