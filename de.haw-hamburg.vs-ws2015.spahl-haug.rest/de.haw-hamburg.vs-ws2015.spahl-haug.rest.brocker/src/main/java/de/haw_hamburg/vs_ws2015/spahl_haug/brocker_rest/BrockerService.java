package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Place;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BankRejectedException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BrockerNotExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.NotSoldException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceAlreadyExistsExeption;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class BrockerService {

	private final Map<String, Brocker> brockers;
	private final String bankServiceURI;
	private final RestTemplate restTemplate = new RestTemplate();

	public BrockerService() throws Exception {
		brockers = new ConcurrentHashMap<>();
		final ServiceRepository serviceRepository = new ServiceRepository();
		bankServiceURI = serviceRepository.getService("bank");
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
		return brockers.put(gameId, new Brocker(gameId, brockerDTO));

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

	public void changeOwner(final String gameId, final String placeid, final Player player) throws PlaceNotFoundException, PlayerDoesntExistsException, BrockerNotExistsException {
		getBrocker(gameId).changeOwner(placeid,player.getId());

	}

	public void buyPlace(final String gameId, final String placeid, final Player player) throws BrockerNotExistsException, PlaceNotFoundException, PlayerDoesntExistsException, BankRejectedException {
		final Place place = getPlace(gameId, placeid);
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

	private void transferMoneyToBank(final String gameId, final int value, final String playerId, final String reason) throws BankRejectedException {
		final ResponseEntity<String> transfer = restTemplate.postForEntity(bankServiceURI + "/" + gameId + "/transfer/from/" + playerId + "/" + value, reason, String.class);
		if(transfer.getStatusCode() != HttpStatus.CREATED){
			throw new BankRejectedException("Bank");
		}

	}

	public void visit(final String gameId, final String placeid, final String playerid) {
		throw new RuntimeException("Not yet Implemented");

	}

}
