package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

public class Player {
	private final String id;
	private final String name;
	private boolean ready;
	private final String playerURI;

	public Player(final String name, final String id, final String playerURI){
		this.name = name;
		this.id = id;
		this.playerURI = playerURI;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(final boolean b) {
		this.ready = b;
	}


	public String getPlayerURI() {
		return playerURI;
	}



}
