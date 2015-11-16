package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.lobby.LobbyWindow;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.login.ILoginActions;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.login.LoginWindow;

public class WindowManager {

	private LoginWindow loginWindow;
	private LobbyWindow lobbyWindow;
	private String userName = "NotLogedIn";

	public void start(){
		disposeAll();
		showLoginWindow();
	}

	public void showLoginWindow(){
		disposeAll();
		if(this.loginWindow == null){
			this.loginWindow = new LoginWindow(new ILoginActions() {

				@Override
				public void onLogin(final String userName) {
					setUserName(userName);
					showLobby();
				}
			});
		}
	}

	public void showLobby(){
		disposeAll();
		if(this.lobbyWindow == null){
			lobbyWindow = new LobbyWindow(getUserName());
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	private void dispose(final IDisposable disposable){
		if(disposable != null){
			disposable.dispose();
		}
	}

	public void disposeAll(){
		dispose(loginWindow);
		loginWindow = null;
	}
}
