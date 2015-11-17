package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

import java.util.List;

import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.GameList;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows.gamelobby.GameLobbyWindow;
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
	private final RestTemplate template = new RestTemplate();
	private final IServiceRepository serviceRepository;
	private final Thread lobbyRefresh;

	public WindowManager(final IApplicationLifecycle lifecycle, final IServiceRepository serviceRepository){
		this.lifecycle = lifecycle;
		this.serviceRepository =serviceRepository;
		this.lobbyRefresh = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean running = true;
				while(running){
					try {
						Thread.sleep(1000);
						updateLobby();
					} catch (final InterruptedException e) {
						running = false;
					}
				}
			}
		});
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
					try {
						template.put(serviceRepository.getService("gamesldt") + "/" + gameId + "/players/" + userName, String.class);
						showGameLobby(gameId);
					} catch (final RestClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void createNewGame() {
					try {
						final Game game = template.postForObject(serviceRepository.getService("gamesldt"), null, Game.class);
						updateLobby();
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
		lobbyRefresh.start();

	}
	private void showGameLobby(final long gameId){
		new GameLobbyWindow(lifecycle, gameId, userName, serviceRepository, loginWindow.getUiThread());
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

	private void updateLobby(){
		final IUiThreadAccess uiThreadAccess = loginWindow.getUiThread();
		try {
			uiThreadAccess.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					try {
						final GameList games = template.getForObject(serviceRepository.getService("gamesldt"), GameList.class);
						lobbyWindow.updateGames(games);
					} catch (final RestClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void disposeAll(){
		dispose(loginWindow);
		//loginWindow = null;
	}
}
