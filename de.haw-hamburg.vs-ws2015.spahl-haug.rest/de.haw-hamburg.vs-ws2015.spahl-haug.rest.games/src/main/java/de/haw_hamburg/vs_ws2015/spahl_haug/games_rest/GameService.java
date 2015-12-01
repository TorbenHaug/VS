package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BoardServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexAllreadyAquiredException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexIsYoursException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;


public class GameService {
	private long nextGameID = 0;
	private final Map<Long, Game> games;
	private final IServiceRepository serviceRepository;
	private final String boardName = "boardsLT";
	private final RestTemplate template = new RestTemplate();

	public GameService(final IServiceRepository serviceRepository){

		this.serviceRepository = serviceRepository;
		this.games = new ConcurrentHashMap<>();
	}

	private long getNextGameID(){
		while(games.get(this.nextGameID) != null){
			this.nextGameID++;
		}
		return this.nextGameID++;
	}

	private void addNewGame(final long id, final Game game) throws BoardServiceNotFoundException{
		this.games.put(id, game);
		String url = null;
		try {
			url = serviceRepository.getService(boardName) + "/" + id;
			try {
				template.put(url,null);
			} catch (final Exception e) {
				this.games.remove(id);
				throw new BoardServiceNotFoundException("No BoardService found " + url + "/" + id);
			}
		} catch (final Exception e1) {
			this.games.remove(id);
			throw new BoardServiceNotFoundException(e1.getMessage());
		}

	}

	public Game createNewGame() throws BoardServiceNotFoundException{
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

	public Player getPlayerFromGame(final long gameID, final String playerID) throws PlayerDoesntExistsException, GameDoesntExistsException{
		final Player player = getGame(gameID).getPlayer(playerID);
		if (player == null){
			throw new PlayerDoesntExistsException("Player " + playerID + " in Game " + gameID + " not found");
		}
		return player;
	}

	public void addPlayerToGame(final long gameID, final String playerID, final String playerName, final String playerURI) throws GameDoesntExistsException, BoardServiceNotFoundException {
		final Player player = new Player(playerName, playerID, playerURI);
		final Game game = getGame(gameID);
		game.addPlayer(player);
		try {
			final String serviceCall = serviceRepository.getService(boardName) + "/" + gameID + "/players/" + playerID;
			System.err.println(serviceCall);
			template.put(serviceCall,null);
		} catch (final Exception e) {
			game.removePlayer(playerID);
			throw new BoardServiceNotFoundException("Unable to add Player " + playerID + " to Board");
		}



	}

	public void signalPlayerReady(final long gameID, final String playerID) throws PlayerDoesntExistsException, GameDoesntExistsException {
		if(!getGame(gameID).isStarted()){
			final Player player = getPlayerFromGame(gameID, playerID);
			player.setReady(true);
			boolean gameStartable = true;
			for(final Player aPlayer: getplayersFromGame(gameID)){
				gameStartable = gameStartable && aPlayer.isReady();
			}
			if(gameStartable){
				getGame(gameID).start();
			}
		}else {
			getGame(gameID).nextTurn();
		}
	}

	public boolean getPlayerReady(final long gameID, final String playerID) throws PlayerDoesntExistsException, GameDoesntExistsException {
		return getPlayerFromGame(gameID, playerID).isReady();
	}

	public List<Game> getAllGames() {
		return new ArrayList<>(games.values());
	}

	public void removePlayerFromGame(final long gameID, final String playerID) throws GameDoesntExistsException {
		getGame(gameID).removePlayer(playerID);
	}

	public List<Player> getplayersFromGame(final long gameID) throws GameDoesntExistsException {
		// TODO Auto-generated method stub
		return getGame(gameID).getPlayers();
	}

	public Player getCurrentPlayer(final long gameID) throws GameNotStartedException, GameDoesntExistsException {
		return getGame(gameID).getCurrentPlayer();
	}

	public Player getMutex(final long gameID) throws GameDoesntExistsException {
		return getGame(gameID).getMutex();
	}

	public void aquireMutex(final long gameID, final String playerId) throws MutexAllreadyAquiredException, MutexIsYoursException, GameDoesntExistsException, PlayerDoesntExistsException {
		getGame(gameID).aquireMutex(playerId);
	}

	public void releaseMutex(final long gameID) throws GameDoesntExistsException {
		getGame(gameID).releaseMutex();

	}

}
