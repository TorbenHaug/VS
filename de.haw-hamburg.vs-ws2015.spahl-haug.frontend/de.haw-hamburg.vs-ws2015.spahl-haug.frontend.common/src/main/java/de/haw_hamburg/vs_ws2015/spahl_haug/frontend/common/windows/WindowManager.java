package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import org.jowidgets.common.application.IApplicationLifecycle;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.lobby.ILobbyActions;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.lobby.LobbyWindow;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.login.ILoginActions;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.login.LoginWindow;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.IServiceRepository;

public class WindowManager {

	private LoginWindow loginWindow;
	private LobbyWindow lobbyWindow;
	private String userName = "NotLogedIn";
	private final IApplicationLifecycle lifecycle;
	final RestTemplate template = new RestTemplate();
	private final IServiceRepository serviceRepository;

	public WindowManager(final IApplicationLifecycle lifecycle, final IServiceRepository serviceRepository){
		this.lifecycle = lifecycle;
		this.serviceRepository =serviceRepository;
	}

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
			}, lifecycle);
		}
	}

	public void showLobby(){
		disposeAll();
		if(this.lobbyWindow == null){
			lobbyWindow = new LobbyWindow(getUserName(), new ILobbyActions() {

				@Override
				public void enterGame(final long gameId) {
					// TODO Auto-generated method stub

				}

				@Override
				public void createNewGame() {
					try {
						System.out.println("newGame");
						final Game game = template.postForObject(serviceRepository.getService("gamesldt"), null, Game.class);
						System.out.println(game);
					} catch (final RestClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			},lifecycle);
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
		//loginWindow = null;
	}
}
