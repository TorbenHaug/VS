package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto;

public class Player {
	private String id;
	private String uri;
	private Place place;

	public Player() {}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(final String uri) {
		this.uri = uri;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(final Place place) {
		this.place = place;
	}




}
