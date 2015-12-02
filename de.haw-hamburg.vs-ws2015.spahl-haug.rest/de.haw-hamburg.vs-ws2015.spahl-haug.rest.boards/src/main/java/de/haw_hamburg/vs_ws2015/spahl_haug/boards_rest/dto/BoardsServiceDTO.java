package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;


public class BoardsServiceDTO {
    private PlayerDTO player;
    private BoardDTO board;

    public BoardsServiceDTO(PlayerDTO player, BoardDTO board) {
        this.player = player;
        this.board = board;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public BoardDTO getBoard() {
        return board;
    }
}
