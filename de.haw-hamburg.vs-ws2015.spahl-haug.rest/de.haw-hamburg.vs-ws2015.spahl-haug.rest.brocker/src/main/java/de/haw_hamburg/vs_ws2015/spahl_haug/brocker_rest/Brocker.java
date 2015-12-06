package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Place;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Player;

public class Brocker {

	private final String gameId;
	private final List<Player> players;
	private final Map<String, Place> places;

	public Brocker(final String gameId, final BrockerDTO brockerDTO) {
		this.gameId = gameId;
		this.players = Collections.synchronizedList(brockerDTO.getPlayers());
		this.places = new ConcurrentHashMap<>();
	}

	public String getGameId() {
		return gameId;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Place> getPlaces() {
		return new ArrayList<>(places.values());
	}


}
