package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class BankServiceNotFoundException extends Exception {
	public BankServiceNotFoundException(final String msg) {
		super(msg);
	}
}
