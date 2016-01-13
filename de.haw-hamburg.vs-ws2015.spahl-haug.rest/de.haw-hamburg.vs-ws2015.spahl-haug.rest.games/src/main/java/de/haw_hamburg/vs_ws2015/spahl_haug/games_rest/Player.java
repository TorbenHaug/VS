package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

public class Player {
	private final String id;
	private final String name;
	private final String playerServiceUri;
	private boolean ready;
	private final String uri;
	private final String readyAdress;
	private String onBank;
	private String onBoard;
	private String onBroker;

	public Player(final String name, final String id, final String uri, final String playerServiceUri){
		this.name = name;
		this.id = id;
		this.uri = uri + "/" + id;
		this.playerServiceUri = playerServiceUri;
		this.readyAdress = this.uri + "/ready";
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(final boolean b) {
		this.ready = b;
	}


	public String getUri() {
		return uri;
	}

	public String getPlayerServiceUri() {
		return playerServiceUri;
	}

	public String getReadyAdress() {
		return readyAdress;
	}

	public String getOnBank() {
		return onBank;
	}

	public void setOnBank(final String onBank) {
		this.onBank = onBank;
	}

	public String getOnBoard() {
		return onBoard;
	}

	public void setOnBoard(final String onBoard) {
		this.onBoard = onBoard;
	}

	public String getOnBroker() {
		return onBroker;
	}

	public void setOnBroker(final String onBroker) {
		this.onBroker = onBroker;
	}



}
