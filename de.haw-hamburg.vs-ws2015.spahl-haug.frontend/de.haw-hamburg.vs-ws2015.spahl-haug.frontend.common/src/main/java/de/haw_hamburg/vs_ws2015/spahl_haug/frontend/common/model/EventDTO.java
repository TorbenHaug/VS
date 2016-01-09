package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class EventDTO {
	private  int id;
	private  String type; // Event Type e.g. bank transfer, rent, got to jail, estate transfer
	private  String name;
	private  String reason;
	private  String resource;
	private  String uri;
	private  String player;

	public EventDTO(){};

	public EventDTO(final String type, final String name, final String reason, final String resource, final int id, final String player) {
		this.type = type;
		this.name = name;
		this.reason = reason;
		this.id = id;
		this.resource = resource;
		this.uri = "/events/" + id;
		this.player = player;
	}

	public int getId() {
		return id;
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

	public String getResource() {
		return resource;
	}

	public String getPlayer() {
		return player;
	}

	public String getUri() {
		return uri;
	}

	@Override
	public String toString() {
		return "Event{" +
				"id=" + id +
				", type='" + type + '\'' +
				", name='" + name + '\'' +
				", reason='" + reason + '\'' +
				", resource='" + resource + '\'' +
				", player=" + player +
				'}';
	}
}
