package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;

public class ErrorMessage {
	private int status;
	private String error;
	private String message;

	public ErrorMessage() {
	}

	public ErrorMessage(final int inStatus, final String inError, final String inMessage)
	{
		status = inStatus;
		error = inError;
		message = inMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(final int inStatus) {
		status = inStatus;
	}

	public String getError() {
		return error;
	}

	public void setError(final String inError) {
		error = inError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String inMessage) {
		message = inMessage;
	}
}