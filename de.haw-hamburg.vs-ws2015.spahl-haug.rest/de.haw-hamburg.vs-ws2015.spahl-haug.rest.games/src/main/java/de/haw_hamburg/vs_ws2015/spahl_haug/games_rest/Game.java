package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private final long id;
	private final List<Player> players;
	
	public Game(long id) {
		super();
		this.id = id;
		this.players = new ArrayList<>();
	}

	public long getId(){
		return this.id;
	}
}
