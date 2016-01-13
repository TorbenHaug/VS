package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Place;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.NotForSaleException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.NotSoldException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceAlreadyExistsExeption;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;

public class Brocker {

	private final String gameId;
	private final String players;
	private final Map<String, Place> places;
	private final RestTemplate restTemplate;

	public Brocker(final String gameId, final BrockerDTO brockerDTO, final RestTemplate restTemplate) {
		this.gameId = gameId;
		this.restTemplate = restTemplate;
		this.players = brockerDTO.getPlayers();
		this.places = new ConcurrentHashMap<>();
	}

	public String getGameId() {
		return gameId;
	}

	public String getPlayers() {
		return players;
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

	public Player getPlayer(final String id) throws PlayerDoesntExistsException {

		try{
			System.out.println("Players: " + getPlayers());
			final String uri = getPlayers() + "/" + id;
			System.out.println("GetPlaYER " + uri);
			final Player player = restTemplate.getForObject(uri, Player.class);
			return player;
		}catch(final RestClientException e){
			throw new PlayerDoesntExistsException("There is no Player " + id + " in game");
		}
	}

	public void changeOwner(final String placeid, final String id) throws PlaceNotFoundException, PlayerDoesntExistsException, NotForSaleException {
		getPlayer(id);
		final Place place = getPlace(placeid);
		if ((place.getOwner() != null) && place.getOwner().equals("NotForSale")){
			throw new NotForSaleException("Not for sale");
		}
		place.setOwner(id);
	}


}
