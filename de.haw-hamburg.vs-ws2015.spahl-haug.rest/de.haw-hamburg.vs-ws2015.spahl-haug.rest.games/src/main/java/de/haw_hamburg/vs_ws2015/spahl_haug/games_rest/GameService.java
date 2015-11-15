package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;


public class GameService {
	private long nextGameID = 0;
	private final Map<Long, Game> games;

	public GameService(){
		this.games = new ConcurrentHashMap<>();
	}

	private long getNextGameID(){
		while(games.get(this.nextGameID) != null){
			this.nextGameID++;
		}
		return this.nextGameID++;
	}

	private void addNewGame(final long id, final Game game){
		this.games.put(id, game);
	}

	public Game createNewGame(){
		final Game game = new Game(getNextGameID());
		this.addNewGame(game.getGameid(), game);
		return game;
	}

	public void deleteGame(final long id){
		this.games.remove(id);
	}

	public Game getGame(final long id) throws GameDoesntExistsException{
		final Game game = this.games.get(id);
		if (game == null){
			throw new GameDoesntExistsException("Game " + id + " not found");
		}
		return game;
	}

	private Player getPlayerFromGame(final long gameID, final long playerID) throws PlayerDoesntExistsException, GameDoesntExistsException{
		final Player player = getGame(gameID).getPlayer(playerID);
		if (player == null){
			throw new PlayerDoesntExistsException("Player " + playerID + " in Game " + gameID + " not found");
		}
		return player;
	}

	public void addPlayerToGame(final long gameID, final long playerID) throws GameDoesntExistsException {
		final Player player = new Player("Name " + playerID, playerID);
		final Game game = getGame(gameID);

		game.addPlayer(player);

	}

	public void signalPlayerReady(final long gameID, final long playerID) throws PlayerDoesntExistsException, GameDoesntExistsException {
		final Player player = getPlayerFromGame(gameID, playerID);
		player.setReady(true);
	}

	public boolean getPlayerReady(final long gameID, final long playerID) throws PlayerDoesntExistsException, GameDoesntExistsException {
		return getPlayerFromGame(gameID, playerID).isReady();
	}

	public List<Game> getAllGames() {
		return new ArrayList<>(games.values());
	}

}
