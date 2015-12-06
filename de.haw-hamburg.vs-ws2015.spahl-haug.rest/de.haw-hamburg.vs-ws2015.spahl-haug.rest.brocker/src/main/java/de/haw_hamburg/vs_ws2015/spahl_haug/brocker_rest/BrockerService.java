package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;

public class BrockerService {

	private final Map<String, Brocker> brockers;

	public BrockerService() {
		brockers = new ConcurrentHashMap<>();
	}

	public Brocker getBrocker(final String gameId) {
		return brockers.get(gameId);

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

	public void getAllPlaces(final String gameId) {
		throw new RuntimeException("Not Yet Implemented");

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
