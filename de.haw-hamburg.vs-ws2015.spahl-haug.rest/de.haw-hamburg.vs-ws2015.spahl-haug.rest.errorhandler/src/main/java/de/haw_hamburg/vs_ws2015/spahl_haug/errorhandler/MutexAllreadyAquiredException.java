package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class MutexAllreadyAquiredException extends Exception {
	public MutexAllreadyAquiredException(final String message) {
		super(message);
	}
}
