package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto;

public class EventDTO {
	private String type; // Event Type e.g. bank transfer, rent, got to jail, estate transfer
	private String name;
	private String reason;
	private String player;
	private String resource;

	public EventDTO() {}

	public EventDTO(final String type, final String name, final String reason,final String resource, final String player) {
		this.type = type;
		this.name = name;
		this.reason = reason;
		this.resource = resource;
		this.player = player;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getReason() {
		return reason;
	}

	public String getPlayer() {
		return player;
	}

	public String getResource() {
		return resource;
	}

}
