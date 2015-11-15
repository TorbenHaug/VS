package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class PlayerDoesntExistsException extends Exception {
	public PlayerDoesntExistsException(final String message) {
		super(message);
	}
}
