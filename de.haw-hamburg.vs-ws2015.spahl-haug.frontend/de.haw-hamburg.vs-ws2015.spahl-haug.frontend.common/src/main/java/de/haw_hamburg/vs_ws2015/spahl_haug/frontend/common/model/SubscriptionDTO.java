package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;

public class SubscriptionDTO {
	private String gameid;
	private String uri;

	public SubscriptionDTO() {}

	public SubscriptionDTO(final String gameid, final String uri) {
		this.gameid = gameid;
		this. uri = uri;
	}

	public String getGameid() {
		return gameid;
	}

	public String getUri() {
		return uri;
	}
}
