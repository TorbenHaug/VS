package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;


import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.restservice.RestService;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class WindowManager {

	LoginWindow loginWindow;
	private final IApplicationLifecycle lifecycle;
	private String userName;
	private LobbyWindow lobbyWindow;
	private final ServiceRepository serviceRepository;
	private final RestTemplate template = new RestTemplate();
	private GameLobbyWindow gameLobbyWindow;
	private GameWindow gameWindow;
	private String gamesService;
	private String boardsService;

	public WindowManager(final IApplicationLifecycle lifecycle, final ServiceRepository serviceRepository) {
		this.lifecycle = lifecycle;
		this.serviceRepository = serviceRepository;
		try {
			this.gamesService = serviceRepository.getService("gamesldt");
			this.boardsService = serviceRepository.getService("boardsldt");
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startWindowing(){
		showLoginWindow();
	}

	private void showLoginWindow(){
		disposeAll();
		loginWindow = new LoginWindow(new ILoginActions() {

			@Override
			public void onLogin(final String userName) {
				setUsername(userName);
				RestService.registerPlayerService(userName, WindowManager.this);
				showLobbyWindow();
			}

			@Override
			public void closeWindow() {
				//disposeAll();
				//lifecycle.finish();

			}
		});
	}


	protected void showLobbyWindow() {
		disposeAll();
		lobbyWindow = new LobbyWindow(userName, new ILobbyActions() {

			@Override
			public void closeWindow() {
				showLoginWindow();
			}

			@Override
			public void enterGame(final String gameId) {
				final MultiValueMap<String,String> params = new LinkedMultiValueMap<String, String>();
				params.add("name", userName);
				params.add("uri", "Test");
				try {
					final UriComponents uriComponents = UriComponentsBuilder
							.fromHttpUrl(serviceRepository.getService("gamesldt") +"/"+ gameId + "/players/" + userName)
							.queryParams(params)
							.build();
					template.put(uriComponents.toUriString(), null);
					showGameLobby(gameId);
				} catch (final RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
			}

			@Override
			public void createGame() {
				try {
					final Game game = template.postForObject(serviceRepository.getService("gamesldt"), null, Game.class);
					enterGame("" + game.getGameid());
				} catch (final RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		},serviceRepository);

	}

	protected void showGameLobby(final String gameId) {
		disposeAll();
		gameLobbyWindow = new GameLobbyWindow(userName, gameId, new IGameLobbyActions() {

			@Override
			public void closeWindow() {
				String url;
				try {
					url = serviceRepository.getService("gamesldt") + "/" + gameId + "/players/" + userName;
					template.delete(url);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				showLobbyWindow();
			}

			@Override
			public void ready(final String gameId) {
				String url;
				try {
					url = serviceRepository.getService("gamesldt") + "/" + gameId + "/players/" + userName + "/ready";
					template.put( url, null);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void startGame(final String gameId) {
				showGameWindow(gameId);

			}
		}, serviceRepository);

	}

	protected void showGameWindow(final String gameId) {
		disposeAll();
		System.out.println("showit");
		gameWindow = new GameWindow(userName, gameId, new IGameActions() {

			@Override
			public void closeWindow() {
				String url;
				try {
					url = serviceRepository.getService("gamesldt") + "/" + gameId + "/players/" + userName;
					template.delete(url);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				showLobbyWindow();

			}
		}, gamesService, boardsService);

	}

	protected void setUsername(final String userName) {
		this.userName = userName;
	}

	private void disposeAll() {
		disposeWindow(loginWindow);
		loginWindow = null;
		disposeWindow(lobbyWindow);
		lobbyWindow = null;
		disposeWindow(gameLobbyWindow);
		gameLobbyWindow = null;
		disposeWindow(gameWindow);
		gameWindow = null;
	}

	private void disposeWindow(final IFrame frame) {
		if(frame != null) {
			frame.setVisible(false);
			frame.dispose();
		}

	}

	public void anounceTurn() {
		if((gameWindow == null) && (gameLobbyWindow != null)){
			gameLobbyWindow.anounceStartGame();
		}
		if(gameWindow == null){
			throw new RuntimeException("No Game available.");
		}
		gameWindow.anounceTurn();
	}

	public void anounceEvent() {
		if((gameWindow == null) && (gameLobbyWindow != null)){
			gameLobbyWindow.anounceStartGame();
		}
		if(gameWindow == null){
			throw new RuntimeException("No Game available.");
		}
		gameWindow.anounceTurn();

	}
}
