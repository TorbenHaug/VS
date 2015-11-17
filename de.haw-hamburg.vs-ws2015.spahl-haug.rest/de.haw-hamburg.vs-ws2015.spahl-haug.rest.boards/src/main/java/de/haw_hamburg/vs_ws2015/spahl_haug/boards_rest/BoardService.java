package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto.GamesDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Louisa on 17.11.2015.
 */
public class BoardService {

	private final Map<Integer, Board> boards = new HashMap<>();

	public BoardService(){
	}
}
