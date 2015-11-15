package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;

import java.util.ArrayList;
import java.util.List;import java.util.function.Predicate;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexAllreadyAquiredException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexIsYoursException;

public class Game {

	private final long gameid;
	private final List<Player> players;
	private boolean started = false;
	private final int currentPlayer = 0;
	private int mutexHolder = -1;

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

	public Player getCurrentPlayer() throws GameNotStartedException {
		if (started) {
			return players.get(currentPlayer);
		} else {
			throw new GameNotStartedException("Game " + getGameid() + " has not stated yet");
		}
	}

	public Player getMutex() {
		if(mutexHolder == -1){
			return null;
		} else {
			return players.get(mutexHolder);
		}
	}

	public void aquireMutex() throws MutexAllreadyAquiredException, MutexIsYoursException {
		if(mutexHolder != -1){
			throw new MutexAllreadyAquiredException("The Mutex is aready aquired by Player " + players.get(mutexHolder));
		} else if (mutexHolder == currentPlayer) {
			throw new MutexIsYoursException();
		}else{
			mutexHolder = currentPlayer;
		}

	}
}
