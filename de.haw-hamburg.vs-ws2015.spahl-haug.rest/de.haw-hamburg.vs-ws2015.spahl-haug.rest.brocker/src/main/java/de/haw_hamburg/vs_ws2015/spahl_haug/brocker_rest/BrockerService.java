package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Place;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BankRejectedException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BrockerNotExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.NotForSaleException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.NotSoldException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceAlreadyExistsExeption;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.RepositoryException;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class BrockerService {

	private final Map<String, Brocker> brockers;
	private final RestTemplate restTemplate = new RestTemplate();
	private Components components;

	public BrockerService(){
		brockers = new ConcurrentHashMap<>();
	}

	private Components getComponents(){
		if (components == null){
			components = new ServiceRepository().getComponents();
		}
		return components;
	}

	public Brocker getBrocker(final String gameId) throws BrockerNotExistsException {
		final Brocker brocker = brockers.get(gameId);
		if(brocker == null){
			throw new BrockerNotExistsException("There's no brocker for game " + gameId);
		}
		return brocker;

	}

	public Brocker createBrocker(final String gameId, final BrockerDTO brockerDTO) throws Exception {
		if(brockers.containsKey(gameId)){
			throw new Exception("Brocker already exists");
		}
		return brockers.put(gameId, new Brocker(gameId, brockerDTO, restTemplate, getComponents().getBoard()));

	}

	public void removeBrocker(final String gameId) {
		brockers.remove(gameId);

	}

	public List<Place> getAllPlaces(final String gameId) throws BrockerNotExistsException {
		return getBrocker(gameId).getPlaces();
	}

	public Place getPlace(final String gameId, final String placeid) throws BrockerNotExistsException, PlaceNotFoundException {
		final Place place = getBrocker(gameId).getPlace(placeid);
		if(place == null){
			throw new PlaceNotFoundException("Place " + placeid + " not found");
		}
		return place;

	}

	public void createPlace(final String gameId, final String placeid, final Place place) throws PlaceAlreadyExistsExeption, BrockerNotExistsException {
		getBrocker(gameId).createPlace(placeid,place);

	}

	public Player getOwner(final String gameId, final String placeid) throws PlaceNotFoundException, BrockerNotExistsException, NotSoldException, PlayerDoesntExistsException {
		return getBrocker(gameId).getOwner(placeid);

	}

	public void changeOwner(final String gameId, final String placeid, final Player player) throws PlaceNotFoundException, PlayerDoesntExistsException, BrockerNotExistsException, NotForSaleException {
		getBrocker(gameId).changeOwner(placeid,player.getId());

	}

	public void buyPlace(final String gameId, final String placeid, final Player player) throws BrockerNotExistsException, PlaceNotFoundException, PlayerDoesntExistsException, BankRejectedException, NotForSaleException, RestClientException, RepositoryException {
		final Place place = getPlace(gameId, placeid);
		if((place.getOwner() != null)){
			throw new NotForSaleException("The Owner is " + place.getOwner());
		}
		getBrocker(gameId).getPlayer(player.getId());
		transferMoneyToBank(gameId, place.getValue(),player.getId(), "Buy a Street!");
		changeOwner(gameId, placeid, player);

	}

	public void hypothecaryCredit(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void deleteHypothecaryCredit(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void visit(final String gameId, final String placeid, final String playerid) throws BrockerNotExistsException, PlaceNotFoundException, PlayerDoesntExistsException, BankRejectedException, RestClientException, RepositoryException {
		final Place place = getPlace(gameId, placeid);
		if((place.getOwner() != null) && !place.getOwner().equals("NotForSale") && !place.getOwner().equals(playerid)){
			getBrocker(gameId).getPlayer(place.getOwner());
			transferMoneyFromPlayerToPlayer(gameId, place.getRent().get(place.getHouses()), playerid, place.getOwner(), "Miete");
			final EventDTO eventDTO = new EventDTO("MoneyTransfer", "MoneyTransfer", "MoneyTransfer", "/banks/" + gameId + "/players/" + playerid, playerid);
			new Thread(){
				@Override
				public void run() {
					try {
						restTemplate.postForLocation(getComponents().getEvents() + "?gameid=" + gameId, eventDTO);
					} catch (final RestClientException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
	}

	private void transferMoneyToBank(final String gameId, final int value, final String playerId, final String reason) throws BankRejectedException, RestClientException, RepositoryException {
		try{
			final ResponseEntity<String> transfer = restTemplate.postForEntity(getComponents().getBank() + "/" + gameId + "/transfer/from/" + playerId + "/" + value, reason, String.class);
		}catch(final RestClientException e){
			throw new BankRejectedException("bank rejected: " + e.getMessage());
		}

	}
	private void transferMoneyFromPlayerToPlayer(final String gameId, final int value, final String playerIdFrom, final String playerIdTo, final String reason) throws BankRejectedException, RestClientException, RepositoryException {
		try{
			final ResponseEntity<String> transfer = restTemplate.postForEntity(getComponents().getBank() + "/" + gameId + "/transfer/from/" + playerIdFrom + "/to/" + playerIdTo + "/" + value, reason, String.class);
		}catch(final RestClientException e){
			throw new BankRejectedException("bank rejected: " + e.getMessage());
		}
	}

}
