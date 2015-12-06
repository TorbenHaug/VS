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
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.NotSoldException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceAlreadyExistsExeption;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;

public class Brocker {

	private final String gameId;
	private final Map<String, Player> players;
	private final Map<String, Place> places;

	public Brocker(final String gameId, final BrockerDTO brockerDTO) {
		this.gameId = gameId;
		this.players = new ConcurrentHashMap<>();
		for(final Player player: brockerDTO.getPlayers()){
			players.put(player.getId(), player);
		}
		this.places = new ConcurrentHashMap<>();
	}

	public String getGameId() {
		return gameId;
	}

	public List<Player> getPlayers() {
		return new ArrayList<Player>(players.values());
	}

	public List<Place> getPlaces() {
		return new ArrayList<>(places.values());
	}

	public Place getPlace(final String placeid) throws PlaceNotFoundException {
		final Place place = places.get(placeid);
		if(place == null){
			throw new PlaceNotFoundException("Place " + placeid + " not found");
		}
		return place;

	}

	public void createPlace(final String placeid, final Place place) throws PlaceAlreadyExistsExeption {
		try {
			getPlace(placeid);
			throw new PlaceAlreadyExistsExeption("Place " + placeid + " already Exists");
		} catch (final PlaceNotFoundException e) {
			places.put(placeid, place);
		}

	}

	public Player getOwner(final String placeid) throws PlaceNotFoundException, NotSoldException, PlayerDoesntExistsException {
		final String owner = getPlace(placeid).getOwner();
		if(owner == null){
			throw new NotSoldException("Place " + placeid + " is not Sold");
		}
		return getPlayer(owner);
	}

	private Player getPlayer(final String owner) throws PlayerDoesntExistsException {
		final Player player = players.get(owner);
		if(player == null){
			throw new PlayerDoesntExistsException("There is no Player " + owner + " in game");
		}
		return player;
	}

	public void changeOwner(final String placeid, final String id) throws PlaceNotFoundException, PlayerDoesntExistsException {
		getPlayer(id);
		final Place place = getPlace(placeid);
		place.setOwner(id);
	}


}
