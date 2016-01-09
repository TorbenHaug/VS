package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PositionNotOnBoardException;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardService {
	// Map<gameId, Board>
	private final Map<Long, Board> boards;
    private final RestTemplate template = new RestTemplate();
    private final IServiceRepository serviceRepository;
    private String eventService;

	public BoardService(final IServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
		this.boards = new HashMap<>();
	}

    private String getEventService() throws EventServiceNotFoundException{
        try{
            if(eventService == null) {
                eventService = serviceRepository.getService("spahl_haug_event");
            }
            return eventService;
        }catch(final Exception e){
            throw new EventServiceNotFoundException("No EventService found");
        }
    }

	@JsonIgnore
	public Board getBoard(final long gameID) {
		return boards.get(gameID);
	}

	public void createBoard(final long gameID) {
		final Board board = new Board();
		this.boards.put(gameID, board);
	}

	public Map<Long, Board> getBoards() {
		return boards;
	}

	public void deleteBoard(final long gameID) {
		boards.remove(gameID);
	}

	public List<Place>  getPlaces(final long gameID) {
		final List<Place> placesMap = new ArrayList<>();

		for(final Field field : boards.get(gameID).getFields()) {
			final Place place = field.getPlace();
			placesMap.add(place);
		}
		return placesMap;
	}

	public void placePlayer(final long gameID, final String playerID) throws PositionNotOnBoardException, PlayerDoesntExistsException {
		boards.get(gameID).setPlayer(playerID);
	}

	public Board placePlayer(final long gameID, final String playerID, final int numOfPosMoves) throws PositionNotOnBoardException, PlayerDoesntExistsException, GameDoesntExistsException {
		final Board board = boards.get(gameID);
		if(board == null){
			throw new GameDoesntExistsException("Board cant find Game");
		}
		board.placePlayerOnPos(playerID, numOfPosMoves);
        placePlacerEvent(String.valueOf(gameID), playerID);
		return board;
	}

    private void placePlacerEvent(String gameID, String playerID) {
        final EventDTO event = new EventDTO("PlayerMovedPosition", "In Game with the ID " + gameID + " Player " + playerID + " moved its position", "PlayerMovedPosition", "boards/" + playerID, playerID);
        new Thread(){
            @Override
            public void run() {
                try {
                    template.postForLocation(getEventService() + "/events?gameid=" + gameID, event);
                } catch (RestClientException | EventServiceNotFoundException e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    public void removePlayerFromBoard(final long gameID, final String playerID) {
		boards.get(gameID).removePlayer(playerID);
	}

	public Player getPlayerPosition(final long gameID, final String playerID) throws PlayerDoesntExistsException {
		return boards.get(gameID).getPosition(playerID);
	}

	@JsonIgnore
	public Player getPlayer(final long gameID, final String playerID) throws PlayerDoesntExistsException {
		return getBoard(gameID).getPlayer(playerID);
	}

	public List<Player> getPlayerFromBoard(final long gameID) {
		return boards.get(gameID).getPlayers();
	}
}
