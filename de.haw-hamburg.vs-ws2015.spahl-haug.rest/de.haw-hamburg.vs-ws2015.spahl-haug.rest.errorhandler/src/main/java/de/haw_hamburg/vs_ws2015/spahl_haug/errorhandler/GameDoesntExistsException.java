package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class GameDoesntExistsException extends Exception {

	private static final long serialVersionUID = 1387908839821736734L;

	public GameDoesntExistsException(final String message) {
		super(message);
	}

}
