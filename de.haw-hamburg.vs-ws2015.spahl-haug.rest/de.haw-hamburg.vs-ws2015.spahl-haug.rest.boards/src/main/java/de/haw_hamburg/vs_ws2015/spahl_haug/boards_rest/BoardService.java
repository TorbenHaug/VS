package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardService {

	private final Map<Long, Board> boards = new HashMap<>();

	public BoardService(){
	}

    public Board getBoard(long gameID) {
        return boards.get(gameID);
    }
}
