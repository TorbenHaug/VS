package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.BoardDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.BoardsServiceDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.BrokerPlaceDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.CreateBankAccountDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.FieldDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.PlayerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.RollsDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BankServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.BrokerServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.EventServiceNotFoundException;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.GameDoesntExistsException;
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


public class BoardService {
	// Map<gameId, Board>
	private final Map<Long, Board> boards;
	private final RestTemplate template = new RestTemplate();
	private Components components;

	public BoardService(final IServiceRepository serviceRepository){
		this.boards = new HashMap<>();
	}



	private Components getComponents(){
		if (components == null){
			components = new ServiceRepository().getComponents();
		}
		return components;
	}



	@JsonIgnore
	public Board getBoard(final long gameID) {
		return boards.get(gameID);
	}

	public void createBoard(final long gameID) throws BankServiceNotFoundException, BrokerServiceNotFoundException {


		try {
			System.err.println("CreateBank: " + getComponents().getBank() + "/" + gameID);
			template.put(getComponents().getBank() + "/" + gameID, null);
		} catch (final RestClientException e) {
			try {
				template.delete(getComponents().getBroker() + "/" + gameID);
			} catch (final RestClientException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new BankServiceNotFoundException(e.getMessage());
		}

		try {
			final BrockerDTO brockerDTO = new BrockerDTO(String.valueOf(gameID), getComponents().getGame() + "/" + gameID, getComponents().getBoard() + "/" + gameID + "/players");
			System.out.println(getComponents().getBroker() + "/" + gameID);
			template.put(getComponents().getBroker()+ "/" + gameID, brockerDTO);
		} catch (final RestClientException  e) {
			throw new BrokerServiceNotFoundException(e.getMessage());
		}
		for(final Place place: Place.values()){
			final BrokerPlaceDTO brokerPlaceDTO = new BrokerPlaceDTO(place.name(), place.getOwner(), place.getValue(), place.getRent(), place.getCost(), place.getHouses());
			try {
				template.put(getComponents().getBroker() + "/" + gameID + "/places/" + place.getPosition(), brokerPlaceDTO);
			} catch (final RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

	public void placePlayer(final long gameID, final String playerID) throws PositionNotOnBoardException, PlayerDoesntExistsException, BankServiceNotFoundException {
		boards.get(gameID).setPlayer(playerID);
		final CreateBankAccountDTO createBankAccountDTO = new CreateBankAccountDTO(playerID, 1000);
		try{
			final String uri = getComponents().getBank() + "/" + gameID + "/players";
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
		placePlacerEvent(String.valueOf(gameID), playerID);
		return board;
	}

	private void placePlacerEvent(final String gameID, final String playerID) {
		final EventDTO event = new EventDTO("PlayerMovedPosition", "In Game with the ID " + gameID + " Player " + playerID + " moved its position", "PlayerMovedPosition", getComponents().getBoard() + "/" + gameID + "/players/" + playerID, playerID);
		new Thread(){
			@Override
			public void run() {
				try {
					template.postForLocation(getComponents().getEvents() + "?gameid=" + gameID, event);
				} catch (final RestClientException e) {
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
				final PlayerDTO playerDTO = new PlayerDTO(player1.getId(), gameID, player1.getPosition());
				playerList.add(playerDTO);
			}
			final FieldDTO fieldDTO = new FieldDTO(gameID, field.getPlace().getPosition(), playerList);
			f.add(fieldDTO);
		}
		final BoardDTO boardDTO = new BoardDTO(f);
		final PlayerDTO playerDTO = new PlayerDTO(player.getId(), gameID, player.getPosition());
		try {
			final String uri = getComponents().getBroker() + "/" + gameID + "/places/" + board.getPosition(playerID).getPosition() + "/visit/" + playerID;
			System.out.println("Call Player Visit: " + uri);
			template.postForLocation(uri, null);
		} catch (final RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BoardsServiceDTO(playerDTO, boardDTO);
	}
}
