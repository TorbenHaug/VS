package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

public class PlaceDTO {
	private final String name;
	private final String uri;
	private final String broker;

	public PlaceDTO(final String name, final String uri, final String broker) {
		this.name = name;
		this.uri = uri;
		this.broker = broker;
	}

	public String getName() {
		return name;
	}

	public String getUri() {
		return uri;
	}

	public String getBroker() {
		return broker;
	}
}
