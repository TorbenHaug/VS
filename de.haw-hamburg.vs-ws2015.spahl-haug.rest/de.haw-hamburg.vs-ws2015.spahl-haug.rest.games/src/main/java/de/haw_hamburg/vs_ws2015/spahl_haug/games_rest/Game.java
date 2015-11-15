package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.ArrayList;
import java.util.List;import java.util.function.Predicate;

public class Game {

	private final long gameid;
	private final List<Player> players;
	private boolean started;
	private final int currentPlayer = 0;

	public Game(final long id) {
		super();
		this.gameid = id;
		this.players = new ArrayList<>();
	}

	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}

	public long getGameid(){
		return this.gameid;
	}

	public void addPlayer(final Player player) {
		if(!players.contains(player)){
			players.add(player);
		}
	}

	public Player getPlayer(final long playerID) {
		for(final Player aPlayer: players){
			if(aPlayer.getId() == playerID){
				return aPlayer;
			}
		}
		return null;
	}

	public void removePlayer(final long playerID) {
		players.removeIf(new Predicate<Player>() {

			@Override
			public boolean test(final Player t) {
				return t.getId() == playerID;
			}
		});
	}

	public void start() {
		this.started = true;
	}
}
