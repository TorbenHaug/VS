package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

public interface IGameLobbyActions  extends IWindowActions{
	public void ready(String gameId);
	public void startGame(String gameId);
}
