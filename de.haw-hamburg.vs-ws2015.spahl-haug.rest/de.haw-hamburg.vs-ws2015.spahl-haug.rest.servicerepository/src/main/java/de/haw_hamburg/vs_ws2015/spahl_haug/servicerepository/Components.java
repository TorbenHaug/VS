package de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository;

public class Components {
	private String game;
	private String dice;
	private String board;
	private String bank;
	private String broker;
	private String events;

	public Components() { }

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

	@Override
	public String toString() {
		return "Components [game=" + game + ", dice=" + dice + ", board=" + board + ", bank=" + bank + ", broker="
				+ broker + ", events=" + events + "]";
	}



}
