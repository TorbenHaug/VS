package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameFullException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;

import java.util.ArrayList;
import java.util.List;import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexAllreadyAquiredException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexIsYoursException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;

public class Game {

	private final long gameid;
	private final List<Player> players;
    private final String uri;
	private boolean started = false;
	private int currentPlayer = 0;
	private int mutexHolder = -1;

	public Game(final long id, String uri) {
		super();
		this.gameid = id;
		this.players = new ArrayList<>();
        this.uri = uri;
	}

	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}

	public long getGameid(){
		return this.gameid;
	}

	public void addPlayer(final Player player) throws GameFullException {
		if(!players.contains(player)){
			if(players.size()>=6){
				throw new GameFullException("More tham 6 Players are not allowed in Game " + getGameid());
			}
			players.add(player);
		}
	}

	@JsonIgnore
	public Player getPlayer(final String playerID) throws PlayerDoesntExistsException {
		for(final Player aPlayer: players){
			if(aPlayer.getId().equals(playerID)){
				return aPlayer;
			}
		}
		return null;
	}

	public void removePlayer(final String playerID) {
		players.removeIf(new Predicate<Player>() {

			@Override
			public boolean test(final Player t) {
				return t.getId().equals(playerID);
			}
		});
	}

	public void start() {
		this.started = true;
	}

	@JsonIgnore
	public Player getCurrentPlayer() throws GameNotStartedException {
		if (started) {
			return players.get(currentPlayer);
		} else {
			throw new GameNotStartedException("Game " + getGameid() + " has not stated yet");
		}
	}

	@JsonIgnore
	public Player getMutex() {
		if(mutexHolder == -1){
			return null;
		} else {
			return players.get(mutexHolder);
		}
	}

	synchronized public void aquireMutex(final String playerId) throws MutexAllreadyAquiredException, MutexIsYoursException, PlayerDoesntExistsException {
		final Player player = this.getPlayer(playerId);
		if(player == null){
			throw new PlayerDoesntExistsException("Player " + playerId + " not in Game");
		}
		final int playerPosInList = this.getPlayers().indexOf(player);
		if(mutexHolder == playerPosInList){
			throw new MutexIsYoursException();
		}
		else if(mutexHolder != -1){
			throw new MutexAllreadyAquiredException("The Mutex is aready aquired by Player " + players.get(mutexHolder));
		}
		else {
			mutexHolder = playerPosInList;
		}
	}

	public void releaseMutex() {
		mutexHolder = -1;
	}

	@JsonIgnore
	public boolean isStarted() {
		return started;
	}

	public Player nextTurn() throws GameNotStartedException {
		currentPlayer = (currentPlayer + 1) % players.size();
		return getCurrentPlayer();
	}

    public String getUri() {
        return uri;
    }
}
