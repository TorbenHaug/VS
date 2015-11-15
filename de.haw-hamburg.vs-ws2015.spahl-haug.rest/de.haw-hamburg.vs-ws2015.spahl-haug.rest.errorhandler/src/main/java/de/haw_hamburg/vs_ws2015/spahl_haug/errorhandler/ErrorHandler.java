package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

public class ErrorHandler {
	@ExceptionHandler(GameDoesntExistsException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage onGameDoesntExists(final GameDoesntExistsException error) {
		return new ErrorMessage(404, "Not Found", error.getMessage());
	}

	@ExceptionHandler(PlayerDoesntExistsException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage onPlayerDoesntExists(final PlayerDoesntExistsException error) {
		return new ErrorMessage(404, "Not Found", error.getMessage());
	}

	@ExceptionHandler(GameNotStartedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorMessage onGameNotStarted(final GameNotStartedException error) {
		return new ErrorMessage(403, "Forbidden", error.getMessage());
	}
}
