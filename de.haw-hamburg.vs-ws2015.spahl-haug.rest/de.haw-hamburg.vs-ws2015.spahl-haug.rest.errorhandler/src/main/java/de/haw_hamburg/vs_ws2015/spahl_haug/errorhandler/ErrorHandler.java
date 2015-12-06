package de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

public class ErrorHandler {

	@ExceptionHandler({
		BrockerNotExistsException.class,
		PlaceNotFoundException.class,
		RepositoryException.class
	})
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage NotFound(final Exception error) {
		return new ErrorMessage(404, "Not Found", error.getMessage());
	}

	@ExceptionHandler({
		PlaceAlreadyExistsExeption.class,
		NotSoldException.class,
		NotForSaleException.class
	})
	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessage alreadyExists(final Exception error) {
		return new ErrorMessage(HttpStatus.CONFLICT.value(), "Confict", error.getMessage());
	}

	@ExceptionHandler(GameDoesntExistsException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage onGameDoesntExists(final GameDoesntExistsException error) {
		return new ErrorMessage(404, "Not Found", error.getMessage());
	}

	@ExceptionHandler(BoardServiceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage onBoardServiceNotFound(final GameDoesntExistsException error) {
		return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Not Found", error.getMessage());
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

	@ExceptionHandler(MutexIsYoursException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ErrorMessage onMutexIsYours(final MutexIsYoursException error) {
		return new ErrorMessage(200, "You have it already", error.getMessage());
	}

	@ExceptionHandler(MutexAllreadyAquiredException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessage onMutexAllreadyAquired(final MutexAllreadyAquiredException error) {
		return new ErrorMessage(409, "Already Auired", error.getMessage());
	}
}
