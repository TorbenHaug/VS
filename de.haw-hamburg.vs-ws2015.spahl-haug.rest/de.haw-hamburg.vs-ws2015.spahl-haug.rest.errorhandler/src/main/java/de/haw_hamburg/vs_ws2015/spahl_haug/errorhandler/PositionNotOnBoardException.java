package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

/**
 * Created by Louisa on 01.12.2015.
 */
public class PositionNotOnBoardException extends Exception {
    public PositionNotOnBoardException(final String message) {
            super(message);
    }
}
