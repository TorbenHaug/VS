package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.ArrayList;
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
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;


public class GameService {
	private long nextGameID = 0;
	private final Map<Long, Game> games;
	private final RestTemplate template = new RestTemplate();
	//	private final String boardService = null;
	//	private String eventService;
	private Components components;

	public GameService(final IServiceRepository serviceRepository){

		this.games = new ConcurrentHashMap<>();
	}

	private Components getComponents(){
		if (components == null){
			components = new ServiceRepository().getComponents();
		}
		return components;
	}

	private long getNextGameID(){
		while(games.get(this.nextGameID) != null){
			this.nextGameID++;
		}
		return this.nextGameID++;
	}

	private void addNewGame(final long id, final Game game) throws BoardServiceNotFoundException{
		this.games.put(id, game);
	}

	public Game createNewGame() throws BoardServiceNotFoundException{
		final Game game = new Game(getNextGameID());
		this.addNewGame(game.getGameid(), game);
		final EventDTO gameCreatedEvent = new EventDTO("CreateNewGame", "The Game with the ID " + game.getGameid() + " is created", "CreateNewGame", getComponents().getGame() + "/" + game.getGameid(), null);
		new Thread(){
			@Override
			public void run() {
				try {
					template.postForLocation(getComponents().getEvents() + "?gameid=nullGame", gameCreatedEvent);
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
		final Player player = new Player(playerName, playerID, playerURI);
		final Game game = getGame(gameID);
		game.addPlayer(player);

		final EventDTO event = new EventDTO("PlayerEnterGame", "The Player " + playerID + " entered Game " + gameID, "PlayerEnterGame", getComponents().getGame() + "/" + game.getGameid(), playerID);
		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents().getEvents() + "?gameid=" + gameID;
					System.out.print("Function addPlayerToGame: POST " + uri);
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
					final String uri = getComponents().getEvents() + "?gameid=nullGame";
					System.out.print("Function addPlayerToGame: POST nullGame " + uri);
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

	private void startGame(final long id) throws BoardServiceNotFoundException, GameDoesntExistsException {
		String url = null;
		url = getComponents().getBoard() + "/" + id;
		System.out.println("startGame BoardserviceUrl: " + url);
		try {
			template.put(url, null);
		} catch (final Exception e) {
			this.games.remove(id);
			throw new BoardServiceNotFoundException("No BoardService found");
		}
		for(final Player aPlayer: getplayersFromGame(id)){
			String serviceCall;
			try {
				serviceCall = getComponents().getBoard() + "/" + id + "/players/" + aPlayer.getId();
				System.err.println("Function startGame: PUT player on board" + serviceCall);
				template.put(serviceCall, null);
			} catch (final Exception e) {
				throw new BoardServiceNotFoundException(e.getMessage());
			}

		}
	}

	private void signalPlayerReadyEvent(final long gameID, final String playerID) {
		final EventDTO event = new EventDTO("PlayerIsReady", "In Game with the ID " + gameID + " the Player " + playerID + " is ready", "PlayerIsReady", getComponents().getGame() + "/" + gameID, playerID);
		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents().getEvents() + "?gameid=" + gameID;
					System.out.print("Function signalPlayerReadyEvent: Player is ready event " + uri);
					template.postForLocation(uri, event);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void signalStartGameEvent(final long gameID) {
		final EventDTO event = new EventDTO("GameHasStarted", "The Game with the ID " + gameID + " has started", "GameHasStarted", getComponents().getGame() + "/" + gameID, null);
		new Thread(){
			@Override
			public void run() {
				try {
					final String uri = getComponents().getEvents() + "?gameid=" + gameID;
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
					final String uri = player.getPlayerURI() + "/player/turn";
					System.out.print("Function anouncePlayerTurn: Player has its turn " + uri);
					template.postForLocation(uri, null);
				}catch(final RestClientException e){
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!Something went wrong in Player anouncement!\n" + e.getStackTrace());
					//throw new PlayerDoesntExistsException("Something went wrong in Player anouncement!.");
				}
			}
		}.start();

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
