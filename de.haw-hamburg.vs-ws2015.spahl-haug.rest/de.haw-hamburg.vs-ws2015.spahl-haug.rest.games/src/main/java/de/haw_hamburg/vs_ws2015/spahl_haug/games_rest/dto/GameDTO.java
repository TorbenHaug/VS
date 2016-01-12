package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;

public class GameDTO {
	private final long gameid;
	private final String players;
	private final String uri;
	private final boolean started;
	private final Components components;


	public GameDTO(final long gameid, final String players, final String uri, final boolean started, final Components components) {
		super();
		this.gameid = gameid;
		this.players = players;
		this.uri = uri;
		this.started = started;
		this.components = components;
	}

	public long getGameid() {
		return gameid;
	}
	public String getPlayers() {
		return players;
	}
	public String getUri() {
		return uri;
	}
	public boolean isStarted() {
		return started;
	}

	public Components getComponents() {
		return components;
	}



}
