package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;

public class GameDTO {
	private  long gameid;
	private  String players;
	private  String uri;
	private  boolean started;
	private  Components components;

	public GameDTO() {
		// TODO Auto-generated constructor stub
	}

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
