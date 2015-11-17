package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardService {

	private Map<Long, Board> boards;

	public BoardService(){
	}

    @JsonIgnore
    public Board getBoard(long gameID) {
        return boards.get(gameID);
    }

    public void createBoard(long gameID) {
        Board board = new Board();
        Map<Long, Board> tmpMap = new HashMap<>();
        tmpMap.put(gameID, board);
        this.boards = tmpMap;
    }

    public Map<Long, Board> getBoards() {
        return boards;
    }

    public void deleteBoard(long gameID) {
        boards.remove(gameID);
    }
}
