package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

public class Player {
	private final String id;
	private final String name;
    private final String uri;
	private boolean ready;

	public Player(final String name, final String id, final String uri){
		this.name = name;
		this.id = id;
		this.uri = uri;
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


	public String getUri() {
		return uri;
	}



}
