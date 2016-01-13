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
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.PlaceDTO;
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
	private final Map<Long, Components> componentsMap;

	public BrockerService(){
		brockers = new ConcurrentHashMap<>();
		componentsMap = new ConcurrentHashMap<>();
	}

	private Components getComponents(final long gameId){
		return componentsMap.get(gameId);
	}


	public Brocker getBrocker(final String gameId) throws BrockerNotExistsException {
		final Brocker brocker = brockers.get(gameId);
		if(brocker == null){
			throw new BrockerNotExistsException("There's no brocker for game " + gameId);
		}
		return brocker;

	}

	public Brocker createBrocker(final String gameId, final BrockerDTO brockerDTO) throws Exception {
		System.err.println("Creating broker " + gameId);
		componentsMap.put(Long.valueOf(gameId), brockerDTO.getComponents());
		System.err.println("Adding Components " + gameId);
		if(brockers.containsKey(gameId)){
			System.err.println("Broker already exists");
			throw new Exception("Broker already exists");
		}
		System.err.println("Instanz Broker " + gameId);
		final Brocker value = new Brocker(gameId, brockerDTO, restTemplate);
		System.err.println("Created broker " + gameId);
		return brockers.put(gameId, value);

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

	public PlaceDTO getPlaceDTO(final String gameId, final String placeid) throws BrockerNotExistsException, PlaceNotFoundException{
		final Place place = getPlace(gameId, placeid);
		return new PlaceDTO(
				getComponents(Long.valueOf(gameId)).getBoard() + "/" + gameId + "/places/" + placeid,
				getComponents(Long.valueOf(gameId)).getBroker() + "/" + gameId + "/places/" + placeid + "/owner",
				place.getValue(),
				place.getRent(),
				place.getCost(),
				place.getHouses(),
				getComponents(Long.valueOf(gameId)).getBroker() + "/" + gameId + "/places/" + placeid + "/visit"
				);
	}

	public void createPlace(final String gameId, final String placeid, final Place place) throws PlaceAlreadyExistsExeption, BrockerNotExistsException {
		getBrocker(gameId).createPlace(placeid,place);

	}

	public Player getOwner(final String gameId, final String placeid) throws PlaceNotFoundException, BrockerNotExistsException, NotSoldException, PlayerDoesntExistsException {
		return getBrocker(gameId).getOwner(placeid);

	}

	public void changeOwner(final String gameId, final String placeid, final Player player) throws PlaceNotFoundException, PlayerDoesntExistsException, BrockerNotExistsException, NotForSaleException {
		System.err.println(player.getId());
		getBrocker(gameId).changeOwner(placeid,player.getId());

	}

	synchronized public void buyPlace(final String gameId, final String placeid, final Player player) throws BrockerNotExistsException, PlaceNotFoundException, PlayerDoesntExistsException, BankRejectedException, NotForSaleException, RestClientException, RepositoryException {
		final Place place = getPlace(gameId, placeid);
		if((place.getOwner() != null)){
			throw new NotForSaleException("The Owner is " + place.getOwner());
		}
		getBrocker(gameId).getPlayer(player.getId());
		transferMoneyToBank(gameId, place.getValue(),player.getId(), "Buy a Street!");
		changeOwner(gameId, placeid, player);
		final EventDTO eventMoney = new EventDTO("MoneyTransfer", "MoneyTransfer", "MoneyTransfer", getComponents(Long.valueOf(gameId)).getBank() + "/" + gameId + "/players/" + player.getId(), player.getId());
		new Thread(){
			@Override
			public void run() {
				try {
					System.err.println("sendEvent: " + eventMoney.getResource());
					restTemplate.postForLocation(getComponents(Long.valueOf(gameId)).getEvents() + "?gameid=" + gameId, eventMoney);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();

		final EventDTO eventField = new EventDTO("ChangeOwner", "ChangeOwner", "ChangeOwner", getComponents(Long.valueOf(gameId)).getBroker() + "/" + gameId + "/places/" + placeid, player.getId());
		new Thread(){
			@Override
			public void run() {
				try {
					System.err.println("sendEvent: " + eventField.getResource());
					restTemplate.postForLocation(getComponents(Long.valueOf(gameId)).getEvents() + "?gameid=" + gameId, eventField);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();

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
			System.out.println("Owner:" +  place.getOwner());
			getBrocker(gameId).getPlayer(place.getOwner());
			transferMoneyFromPlayerToPlayer(gameId, place.getRent().get(place.getHouses()), playerid, place.getOwner(), "Miete");
			final EventDTO eventDTO = new EventDTO("MoneyTransfer", "MoneyTransfer", "MoneyTransfer", getComponents(Long.valueOf(gameId)).getBank() + "/" + gameId + "/players/" + playerid, playerid);
			new Thread(){
				@Override
				public void run() {
					try {
						System.err.println("sendEvent: " + eventDTO.getResource());
						restTemplate.postForLocation(getComponents(Long.valueOf(gameId)).getEvents() + "?gameid=" + gameId, eventDTO);
					} catch (final RestClientException e) {
						e.printStackTrace();
					}
				};
			}.start();

			final EventDTO eventDTO2 = new EventDTO("MoneyTransfer", "MoneyTransfer", "MoneyTransfer", getComponents(Long.valueOf(gameId)).getBank() + "/" + gameId + "/players/" + place.getOwner(), place.getOwner());
			new Thread(){
				@Override
				public void run() {
					try {
						System.err.println("sendEvent: " + eventDTO2.getResource());
						restTemplate.postForLocation(getComponents(Long.valueOf(gameId)).getEvents() + "?gameid=" + gameId, eventDTO2);
					} catch (final RestClientException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
	}

	private void transferMoneyToBank(final String gameId, final int value, final String playerId, final String reason) throws BankRejectedException, RestClientException, RepositoryException {
		try{
			final ResponseEntity<String> transfer = restTemplate.postForEntity(getComponents(Long.valueOf(gameId)).getBank() + "/" + gameId + "/transfer/from/" + playerId + "/" + value, reason, String.class);
		}catch(final RestClientException e){
			throw new BankRejectedException("bank rejected: " + e.getMessage());
		}

	}
	private void transferMoneyFromPlayerToPlayer(final String gameId, final int value, final String playerIdFrom, final String playerIdTo, final String reason) throws BankRejectedException, RestClientException, RepositoryException {
		try{
			final ResponseEntity<String> transfer = restTemplate.postForEntity(getComponents(Long.valueOf(gameId)).getBank() + "/" + gameId + "/transfer/from/" + playerIdFrom + "/to/" + playerIdTo + "/" + value, reason, String.class);
		}catch(final RestClientException e){
			throw new BankRejectedException("bank rejected: " + e.getMessage());
		}
	}

}
