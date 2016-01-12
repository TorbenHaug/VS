package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;

import java.util.ArrayList;
import java.util.List;


public class Game {

	private long gameid;
	private List<Player> players = new ArrayList<Player>();
	private boolean started;

	public long getGameid() {
		return gameid;
	}
	public void setGameid(final long gameid) {
		this.gameid = gameid;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(final List<Player> players) {
		this.players = players;
	}

	public boolean isStarted() {
		return started;
	}
	public void setStarted(final boolean started) {
		this.started = started;
	}
	@Override
	public String toString() {
		return "Game [gameid=" + gameid + ", players=" + players + ", started=" + started + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (gameid ^ (gameid >>> 32));
		result = (prime * result) + ((players == null) ? 0 : players.hashCode());
		result = (prime * result) + (started ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Game other = (Game) obj;
		if (gameid != other.gameid) {
			return false;
		}
		if (players == null) {
			if (other.players != null) {
				return false;
			}
		} else if (!players.equals(other.players)) {
			return false;
		}
		if (started != other.started) {
			return false;
		}
		return true;
	}







}