package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BankServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BoardServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameFullException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameNotStartedException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexAllreadyAquiredException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.MutexIsYoursException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.GamesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;


public class GameService {
	private long nextGameID = 0;
	private final Map<Long, Game> games;
	private final RestTemplate template = new RestTemplate();
	private final Map<Long, Components> componentsMap;

	public GameService(){
		this.games = new ConcurrentHashMap<>();
		this.componentsMap = new ConcurrentHashMap<>();
	}

	private Components getComponents(final long gameId){
		return componentsMap.get(gameId);
	}

	private long getNextGameID(){
		while(games.get(this.nextGameID) != null){
			this.nextGameID++;
		}
		return this.nextGameID++;
	}

	private void addNewGame(final long id, final Game game) throws BoardServiceNotFoundException {
		this.games.put(id, game);
	}

	public Game createNewGame(final Components components) throws BoardServiceNotFoundException {
		final long nextGameId = getNextGameID();
		componentsMap.put(nextGameId, components);
		final String gameUri = getComponents(nextGameId).getGame() + "/" + nextGameId;
		System.err.println("Function createNewGame: creating new game with resource " + gameUri);
		final Game game = new Game(nextGameId, gameUri, components);
		this.addNewGame(game.getGameid(), game);
		final EventDTO gameCreatedEvent = new EventDTO("CreateNewGame", "The Game with the ID " + game.getGameid() + " is created", "CreateNewGame", getComponents(game.getGameid()).getGame() + "/" + game.getGameid(), null);
		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents(game.getGameid()).getEvents() + "?gameid=nullGame";
					System.err.println("Function createNewGame: with uri " + uri);
					template.postForLocation(uri, gameCreatedEvent);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();
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

	public void addPlayerToGame(final long gameID, final String playerID, final String playerName, final String playerURI) throws GameDoesntExistsException, BoardServiceNotFoundException, GameFullException {
		final Game game = getGame(gameID);
		final Player player = new Player(playerName, playerID, game.getUri() + "/players", playerURI);

		game.addPlayer(player);

		final EventDTO event = new EventDTO("PlayerEnterGame", "The Player " + playerID + " entered Game " + gameID, "PlayerEnterGame", getComponents(gameID).getGame() + "/" + game.getGameid(), playerID);
		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents(gameID).getEvents() + "?gameid=" + gameID;
					System.err.print("Function addPlayerToGame: POST " + uri);
					template.postForLocation(uri, event);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();

		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents(gameID).getEvents() + "?gameid=nullGame";
					System.err.print("Function addPlayerToGame: POST nullGame " + uri);
					template.postForLocation(uri, event);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	synchronized public void signalPlayerReady(final long gameID, final String playerID) throws PlayerDoesntExistsException, GameDoesntExistsException, GameNotStartedException, BoardServiceNotFoundException {
		if(!getGame(gameID).isStarted()){
			final Player player = getPlayerFromGame(gameID, playerID);
			player.setReady(true);
			signalPlayerReadyEvent(gameID, playerID);
			if(getplayersFromGame(gameID).size() > 1){
				System.out.println("Players on Start: " + getplayersFromGame(gameID));
				boolean gameStartable = true;
				for(final Player aPlayer: getplayersFromGame(gameID)){
					gameStartable = gameStartable && aPlayer.isReady();
				}
				if(gameStartable){
					getGame(gameID).start();
					final Player firstPlayer = getCurrentPlayer(gameID);
					startGame(gameID);
					signalStartGameEvent(gameID);
					anouncePlayerTurn(firstPlayer);
				}
			}
		}else {
			final Player player = getGame(gameID).nextTurn();
			anouncePlayerTurn(player);
		}
	}

	private void startGame(final long gameId) throws BoardServiceNotFoundException, GameDoesntExistsException {
		String url = null;
		url = getComponents(gameId).getBoard() + "/" + gameId;
		System.out.println("startGame BoardserviceUrl: " + url);
		final Components request = getComponents(gameId);
		try {
			template.put(url, request);
		} catch (final Exception e) {
			this.games.remove(gameId);
			throw new BoardServiceNotFoundException("No BoardService found");
		}
		for(final Player aPlayer: getplayersFromGame(gameId)){
			try {
				final String onBoard = getComponents(gameId).getBoard() + "/" + gameId + "/players/" + aPlayer.getId();
				final String onBank = getComponents(gameId).getBank() + "/" + gameId + "/players/" + aPlayer.getId();
				final String onBroker = getComponents(gameId).getBank() + "/" + gameId + "/players/" + aPlayer.getId();
				System.err.println("Function startGame: PUT player on board" + onBoard);
				template.put(onBoard, null);
				aPlayer.setOnBoard(onBoard);
				aPlayer.setOnBank(onBank);
				aPlayer.setOnBroker(onBroker);
			} catch (final Exception e) {
				throw new BoardServiceNotFoundException(e.getMessage());
			}

		}
	}

	private void signalPlayerReadyEvent(final long gameId, final String playerID) {
		final EventDTO event = new EventDTO("PlayerIsReady", "In Game with the ID " + gameId + " the Player " + playerID + " is ready", "PlayerIsReady", getComponents(gameId).getGame() + "/" + gameId, playerID);
		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents(gameId).getEvents() + "?gameid=" + gameId;
					System.out.print("Function signalPlayerReadyEvent: Player is ready event " + uri);
					template.postForLocation(uri, event);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void signalStartGameEvent(final long gameId) {
		final EventDTO event = new EventDTO("GameHasStarted", "The Game with the ID " + gameId + " has started", "GameHasStarted", getComponents(gameId).getGame() + "/" + gameId, null);
		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents(gameId).getEvents() + "?gameid=" + gameId;
					System.out.print("Function signalStartGameEvent: Game has started event " + uri);
					template.postForLocation(uri, event);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void anouncePlayerTurn(final Player player) throws PlayerDoesntExistsException{
		new Thread(){
			@Override
			public void run() {
				try{
					final String uri = player.getPlayerServiceUri() + "/player/turn";
					System.out.print("Function anouncePlayerTurn: Player has its turn " + uri);
					template.postForLocation(uri, null);
				}catch(final RestClientException e){
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!Something went wrong in Player anouncement!\n" + e.getStackTrace());
					//throw new PlayerDoesntExistsException("Something went wrong in Player anouncement!.");
				}
			}
		}.start();

	}

	public boolean getPlayerReady(final long gameId, final String playerID) throws PlayerDoesntExistsException, GameDoesntExistsException {
		return getPlayerFromGame(gameId, playerID).isReady();
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
