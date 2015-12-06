package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class GameHasNoEventsException extends Exception {
    public GameHasNoEventsException(final String message) {
        super(message);
    }
}
