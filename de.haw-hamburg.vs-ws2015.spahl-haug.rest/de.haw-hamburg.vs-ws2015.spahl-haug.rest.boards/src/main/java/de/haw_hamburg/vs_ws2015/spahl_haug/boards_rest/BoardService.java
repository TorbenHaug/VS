package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.*;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BankServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BrokerServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlaceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PlayerDoesntExistsException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.PositionNotOnBoardException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.RollnumberNotAcceptableException;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BoardService {
	// Map<gameId, Board>
	private final Map<Long, Board> boards;
	private final RestTemplate template = new RestTemplate();
	private final Map<Long, Components> componentsMap;

	public BoardService(){
		this.boards = new ConcurrentHashMap<>();
		this.componentsMap = new ConcurrentHashMap<>();
	}

	private Components getComponents(final long gameId){
		return componentsMap.get(gameId);
	}


	@JsonIgnore
	public Board getBoard(final long gameID) {
		return boards.get(gameID);
	}

	public void createBoard(final long gameID,  final Components components) throws BankServiceNotFoundException, BrokerServiceNotFoundException {
		componentsMap.put(gameID, components);
		final Components componentsForGame = getComponents(gameID);
		try {
			final String uri = componentsForGame.getBank() + "/" + gameID;
			System.err.println("CreateBank: " + uri);
			final Components request = componentsForGame;
			template.put(uri, request);
		} catch (final RestClientException e) {
			try {
				template.delete(componentsForGame.getBroker() + "/" + gameID);
			} catch (final RestClientException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new BankServiceNotFoundException(e.getMessage());
		}

		try {
			final BrockerDTO brockerDTO = new BrockerDTO(String.valueOf(gameID), componentsForGame.getGame() + "/" + gameID, getComponents(gameID).getBoard() + "/" + gameID + "/players", componentsForGame);
			final String uri = componentsForGame.getBroker()+ "/" + gameID;
			System.err.println("Creating: " + uri);
			System.err.println("BrokerDTo Components: " + brockerDTO.getComponents().toString());
			template.put(uri, brockerDTO);
		} catch (final RestClientException  e) {
			throw new BrokerServiceNotFoundException(e.getMessage());
		}
		for(final Place place: Place.values()){
			final BrokerPlaceDTO brokerPlaceDTO = new BrokerPlaceDTO(place.name(), place.getOwner(), place.getValue(), place.getRent(), place.getCost(), place.getHouses());
			try {
				template.put(getComponents(gameID).getBroker() + "/" + gameID + "/places/" + place.getPosition(), brokerPlaceDTO);
			} catch (final RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final String boardUri = getComponents(gameID).getBoard() + "/" + gameID;
		final Board board = new Board(boardUri);
		this.boards.put(gameID, board);

	}

	public Map<Long, Board> getBoards() {
		return boards;
	}

	public void deleteBoard(final long gameID) {
		boards.remove(gameID);
	}

	public List<Place> getPlaces(final long gameID) {
		final List<Place> placesMap = new ArrayList<>();

		for(final Field field : boards.get(gameID).getFields()) {
			final Place place = field.getPlace();
			placesMap.add(place);
		}
		return placesMap;
	}

	public void placePlayer(final long gameID, final String playerID) throws PositionNotOnBoardException, PlayerDoesntExistsException, BankServiceNotFoundException {
		final String playerUri = getComponents(gameID).getBoard() + "/players/" + playerID;
		boards.get(gameID).setPlayer(playerID, playerUri);
		final CreateBankAccountDTO createBankAccountDTO = new CreateBankAccountDTO(playerID, 1000);
		try{
			final String uri = getComponents(gameID).getBank() + "/" + gameID + "/players";
			System.out.println(uri);
			template.postForLocation(uri, createBankAccountDTO);
		}catch(final RestClientException e){
			removePlayerFromBoard(gameID, playerID);
			throw new BankServiceNotFoundException(e.getMessage());
		}
	}

	public Board placePlayer(final long gameID, final String playerID, final int numOfPosMoves) throws PositionNotOnBoardException, PlayerDoesntExistsException, GameDoesntExistsException {
		final Board board = boards.get(gameID);
		if(board == null){
			throw new GameDoesntExistsException("Board cant find Game");
		}
		board.placePlayerOnPos(playerID, numOfPosMoves);
		placePlayerEvent(gameID, playerID);
		return board;
	}


	private void placePlayerEvent(final long gameID, final String playerID) {
		//		final EventDTO event = new EventDTO("PlayerMovedPosition", "In Game with the ID " + gameID + " Player " + playerID + " moved its position", "PlayerMovedPosition", "boards/" + playerID, playerID);
		final EventDTO event = new EventDTO("PlayerMovedPosition", "In Game with the ID " + gameID + " Player " + playerID + " moved its position", "PlayerMovedPosition", getComponents(gameID).getBoard() + "/" + gameID + "/players/" + playerID, playerID);

		new Thread(){
			@Override
			public void run() {
				try {
					template.postForLocation(getComponents(gameID).getEvents() + "?gameid=" + gameID, event);
				} catch (final RestClientException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public void removePlayerFromBoard(final long gameID, final String playerID) {
		boards.get(gameID).removePlayer(playerID);
	}

	public PlayerDTO getPlayerPosition(final long gameID, final String playerID) throws PlayerDoesntExistsException {
		final Player p = boards.get(gameID).getPosition(playerID);
		final PlayerDTO player = new PlayerDTO(playerID, gameID, p.getPosition(), p.getUri(), getComponents(gameID).getBoard());
		return player;
	}

	@JsonIgnore
	public Player getPlayer(final long gameID, final String playerID) throws PlayerDoesntExistsException {
		return getBoard(gameID).getPlayer(playerID);
	}

	public List<Player> getPlayerFromBoard(final long gameID) {
		return boards.get(gameID).getPlayers();
	}

	public BoardsServiceDTO movePlayer(final RollsDTO roll, final long gameID, final String playerID) throws RollnumberNotAcceptableException, PositionNotOnBoardException, PlayerDoesntExistsException, GameDoesntExistsException {
		final int roll1 = roll.getRoll1().getNumber();
		final int roll2 = roll.getRoll2().getNumber();
		if(((roll1 < 1) || (roll1 > 6)) || ((roll2 < 1) || (roll2 > 6))) {
			throw new RollnumberNotAcceptableException("The Roll numbers are not in the range 1 to 6");
		}
		final int rollSum = roll1 + roll2;
		final Board board = placePlayer(gameID, playerID, rollSum);

		final Player player = board.getPlayer(playerID);
		final List<Field> fields = board.getFields();
		final List<FieldDTO> f = new ArrayList<>();
		for(final Field field : fields) {
			final List<PlayerDTO> playerList = new ArrayList<>();
			for(final Player player1 : field.getPlayers()){
				final PlayerDTO playerDTO = new PlayerDTO(player1.getId(), gameID, player1.getPosition(), player1.getUri(), getComponents(gameID).getBoard());
				playerList.add(playerDTO);
			}
			final FieldDTO fieldDTO = new FieldDTO(gameID, field.getPlace().getPosition(), playerList);
			f.add(fieldDTO);
		}
		final BoardDTO boardDTO = new BoardDTO(f);
		final PlayerDTO playerDTO = new PlayerDTO(player.getId(), gameID, player.getPosition(), player.getUri(), getComponents(gameID).getBoard());
		try {
			final String uri = getComponents(gameID).getBroker() + "/" + gameID + "/places/" + board.getPosition(playerID).getPosition() + "/visit/" + playerID;
			System.out.println("Call Player Visit: " + uri);
			template.postForLocation(uri, null);
		} catch (final RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BoardsServiceDTO(playerDTO, boardDTO);
	}

	public List<PlaceDTO> getAvailablePlacesOnBoard(final long gameID) {
		final List<PlaceDTO> placeList = new ArrayList<>();
		for(final Place place : getPlaces(gameID)) {
			final String placeUri = getComponents(gameID).getBoard() + "/places/" + place.getPosition();
			final PlaceDTO placeDTO = new PlaceDTO(place.toString(), placeUri, getComponents(gameID).getBroker() + "/" + gameID + "/places/" + place.getPosition());
			placeList.add(placeDTO);
		}
		return placeList;
	}

	public PlaceDTO getPlace(final long gameID, final long placeID) throws PlaceNotFoundException {
		for(final Place place: getPlaces(gameID)){
			if(place.getPosition() == placeID){
				return new PlaceDTO(place.name(), getComponents(gameID).getBoard() + "/places/" + place.getPosition(), getComponents(gameID).getBroker() + "/" + gameID + "/places/" + place.getPosition());
			}
		}
		throw new PlaceNotFoundException("Place " + placeID + " not found");
	}
}
