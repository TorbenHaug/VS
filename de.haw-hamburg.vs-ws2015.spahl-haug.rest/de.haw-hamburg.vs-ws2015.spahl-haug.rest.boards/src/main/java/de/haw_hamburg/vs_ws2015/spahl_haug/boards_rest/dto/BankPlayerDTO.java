package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

public class BankPlayerDTO {
	private final String id;
	private final String name = "dummy";
	private final String uri = "dummy";
	private final boolean isReady = false;
	public BankPlayerDTO(final String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}


}
