package de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository;

public class Components {
	private final String game;
	private final String dice;
	private final String board;
	private final String bank;
	private final String broker;
	private final String events;
	public Components(final String game, final String dice, final String board, final String bank, final String broker, final String events) {
		super();
		this.game = game;
		this.dice = dice;
		this.board = board;
		this.bank = bank;
		this.broker = broker;
		this.events = events;
	}
	public String getGame() {
		return game;
	}
	public String getDice() {
		return dice;
	}
	public String getBoard() {
		return board;
	}
	public String getBank() {
		return bank;
	}
	public String getBroker() {
		return broker;
	}
	public String getEvents() {
		return events;
	}


}
