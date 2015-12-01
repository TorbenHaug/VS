package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.Board;

public class BoardsDTO {
	List<Board> boards;

	public BoardsDTO(final List<Board> boards) {
		super();
		this.boards = boards;
	}

	public BoardsDTO() {
		super();
	}

	public List<Board> getBoards() {
		return boards;
	}

	public void setBoards(final List<Board> boards) {
		this.boards = boards;
	}



}
