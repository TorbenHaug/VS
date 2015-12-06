package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.ArrayList;
import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Player;

public class Brocker {

	private final String gameId;
	private final List<Player> players;

	public Brocker(final String gameId, final BrockerDTO brockerDTO) {
		this.gameId = gameId;
		this.players = new ArrayList<Player>(brockerDTO.getPlayers());
	}

}
