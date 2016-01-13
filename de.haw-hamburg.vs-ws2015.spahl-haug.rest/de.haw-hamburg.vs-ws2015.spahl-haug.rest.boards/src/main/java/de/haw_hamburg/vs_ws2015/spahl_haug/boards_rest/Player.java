package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;


public class Player {
	private final String id;
	private final String uri;
	private final String roll;
	private final String move;
	private Place place;
	private int position;

	public Player(final String id, final String uri) {
		this.id = id;
		this.uri = uri;
		this.roll = uri + "/roll";
		this.move = uri + "/move";
	}

	public String getId() {
		return id;
	}

	public Place getPlace() {
		return place;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setPlace(final Place place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "Player{" +
				"id='" + id + '\'' +
				", place=" + place +
				", position=" + position +
				'}';
	}

	public String getUri() {
		return uri;
	}

	public String getRoll() {
		return roll;
	}

	public String getMove() {
		return move;
	}
}
