package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

public class CreateBankAccountDTO {
	BankPlayerDTO player;
	int saldo;
	public CreateBankAccountDTO(final String player, final int saldo) {
		super();
		this.player = new BankPlayerDTO(player);
		this.saldo = saldo;
	}
	public BankPlayerDTO getPlayer() {
		return player;
	}
	public int getSaldo() {
		return saldo;
	}


}
