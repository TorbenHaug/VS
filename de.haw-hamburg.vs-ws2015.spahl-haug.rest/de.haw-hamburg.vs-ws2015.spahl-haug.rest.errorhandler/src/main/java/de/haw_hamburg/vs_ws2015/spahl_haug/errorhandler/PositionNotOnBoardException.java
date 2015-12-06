package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class PositionNotOnBoardException extends Exception {
    public PositionNotOnBoardException(final String message) {
            super(message);
    }
}
