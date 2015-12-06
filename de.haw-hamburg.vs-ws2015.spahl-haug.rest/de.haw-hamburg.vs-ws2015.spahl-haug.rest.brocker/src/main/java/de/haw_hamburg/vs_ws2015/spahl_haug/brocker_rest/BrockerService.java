package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Place;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BrockerNotExistsException;

public class BrockerService {

	private final Map<String, Brocker> brockers;

	public BrockerService() {
		brockers = new ConcurrentHashMap<>();
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

	public void getPlace(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void createPlace(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void getOwner(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void changeOwner(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void buyPlace(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void hypothecaryCredit(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

	public void deleteHypothecaryCredit(final String gameId, final String placeid) {
		throw new RuntimeException("Not yet Implemented");

	}

}
