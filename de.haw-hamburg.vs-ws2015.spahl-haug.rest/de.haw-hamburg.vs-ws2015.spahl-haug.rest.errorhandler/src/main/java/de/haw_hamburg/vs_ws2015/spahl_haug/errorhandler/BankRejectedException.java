package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class BankRejectedException extends Exception {
	public BankRejectedException(final String msg) {
		super(msg);
	}
}
