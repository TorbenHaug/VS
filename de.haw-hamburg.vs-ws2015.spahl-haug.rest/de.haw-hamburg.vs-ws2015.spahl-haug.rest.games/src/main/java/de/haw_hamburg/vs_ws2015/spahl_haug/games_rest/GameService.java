package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	
	private void addNewGame(long id, Game game){
		this.games.put(id, game);
	}
	
	public Game createNewGame(){
		Game game = new Game(getNextGameID());
		this.addNewGame(game.getId(), game);
		return game;
	}
	
	public void deleteGame(long id){
		this.games.remove(id);
	}
	
	public Game getGame(long id){
		return this.games.get(id);
	}
	
}
