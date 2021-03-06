package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import java.util.Enumeration;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.EventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.Game;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.SubscriptionDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model.SubscriptionEventDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.restservice.RestService;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.Components;
import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.ServiceRepository;

public class WindowManager {

	protected static final int SERVER_PORT = 4567;
	LoginWindow loginWindow;
	private final IApplicationLifecycle lifecycle;
	private String userName;
	private LobbyWindow lobbyWindow;
	private final ServiceRepository serviceRepository;
	private final RestTemplate template = new RestTemplate();
	private GameLobbyWindow gameLobbyWindow;
	private GameWindow gameWindow;
	IUiThreadAccess uiThreadAccess;
	private final Components components;

	public WindowManager(final IApplicationLifecycle lifecycle, final ServiceRepository serviceRepository) {
		this.lifecycle = lifecycle;
		this.serviceRepository = serviceRepository;
		this.components = serviceRepository.getComponents();
	}

	public void startWindowing(){
		showLoginWindow();
		uiThreadAccess = loginWindow.getUIThread();
	}

	private void showLoginWindow(){
		disposeAll();
		loginWindow = new LoginWindow(new ILoginActions() {

			@Override
			public void onLogin(final String userName) throws RepositoryException {
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


	protected void showLobbyWindow() throws RepositoryException {
		disposeAll();
		lobbyWindow = new LobbyWindow(userName, new ILobbyActions() {

			@Override
			public void closeWindow() {
				showLoginWindow();
			}

			@Override
			public void enterGame(final String gameURI) {
				final MultiValueMap<String,String> params = new LinkedMultiValueMap<String, String>();
				params.add("name", userName);
				try {
					params.add("uri", "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName);
				} catch (final UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


				try {
					final UriComponents putPlayerURI = UriComponentsBuilder
							.fromHttpUrl(gameURI + "/players/" + userName)
							.queryParams(params)
							.build();
					System.out.println(putPlayerURI.toString());
					System.out.println(params);
					template.put(putPlayerURI.toUriString(), null);
					//showGameLobby(gameURI);
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
					System.out.println(components);
					template.postForLocation(components.getGame(), components);
					//enterGame("" + game.getGameid());
				} catch (final RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		},components.getGame());

		try {
			SubscriptionDTO subscriptionDTO = new SubscriptionDTO("nullGame", "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("CreateNewGame"));
			template.postForLocation(components.getEvents() + "/subscriptions", subscriptionDTO);
			subscriptionDTO = new SubscriptionDTO("nullGame", "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("PlayerEnterGame"));
			template.postForLocation(components.getEvents()  + "/subscriptions", subscriptionDTO);
		} catch (final RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void showGameLobby(final String gameURI) throws RepositoryException {
		disposeAll();
		uiThreadAccess.invokeLater(new Runnable() {

			@Override
			public void run() {
				gameLobbyWindow = new GameLobbyWindow(userName, gameURI, new IGameLobbyActions() {

					@Override
					public void closeWindow() throws RepositoryException {
						String url;
						try {
							url = gameURI + "/players/" + userName;
							template.delete(url);
						} catch (final Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						showLobbyWindow();
					}

					@Override
					public void ready(final String gameURI) {
						final String url;
						try {
							url = gameURI + "/players/" + userName + "/ready";
							new Thread(){
								@Override
								public void run() {
									template.put( url, String.class);
								}
							}.start();
						} catch (final Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void startGame(final String gameId) {
						try {
							showGameWindow(gameId);
						} catch (final RepositoryException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, components.getGame());

			}
		});

		SubscriptionDTO subscriptionDTO;
		try {
			final String[] split = gameURI.split("/");
			final String gameId = split[split.length-1];
			System.err.println(gameId);
			subscriptionDTO = new SubscriptionDTO(gameId, "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("PlayerEnterGame"));
			template.postForLocation(components.getEvents() + "/subscriptions", subscriptionDTO);
			subscriptionDTO = new SubscriptionDTO(gameId, "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("PlayerIsReady"));
			template.postForLocation(components.getEvents() + "/subscriptions", subscriptionDTO);
			subscriptionDTO = new SubscriptionDTO(gameId, "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("GameHasStarted"));
			template.postForLocation(components.getEvents() + "/subscriptions", subscriptionDTO);
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	protected void showGameWindow(final String gameURI) throws RepositoryException {
		disposeAll();
		System.out.println("showit");
		gameWindow = new GameWindow(userName, gameURI, new IGameActions() {

			@Override
			public void closeWindow() throws RepositoryException {
				String url;
				try {
					url = gameURI + "/players/" + userName;
					template.delete(url);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				showLobbyWindow();

			}
		}, components.getGame(), components.getBoard(), components.getDice(), components.getBroker(), components.getBank());

		SubscriptionDTO subscriptionDTO;
		try {
			final String[] split = gameURI.split("/");
			final String gameId = split[split.length-1];
			System.err.println(gameId);
			subscriptionDTO = new SubscriptionDTO(gameId, "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("PlayerMovedPosition"));
			template.postForLocation(components.getEvents() + "/subscriptions", subscriptionDTO);
			subscriptionDTO = new SubscriptionDTO(gameId, "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("MoneyTransfer"));
			template.postForLocation(components.getEvents() + "/subscriptions", subscriptionDTO);
			subscriptionDTO = new SubscriptionDTO(gameId, "http://" + getLocalHostLANAddress().getHostAddress() + ":" + SERVER_PORT + "/monopolyrwt/playerservice/" + userName + "/player/event", new SubscriptionEventDTO("ChangeOwner"));
			template.postForLocation(components.getEvents() + "/subscriptions", subscriptionDTO);
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private void disposeWindow(final IMyFrame frame) {
		if(frame != null){
			frame.getUIThread().invokeLater(new Runnable() {

				@Override
				public void run() {
					if(!frame.isDisposed()) {
						System.out.println("Dispose: " + frame);
						frame.setVisible(false);
						frame.dispose();
					}
				}
			});
		}

	}

	public void anounceTurn() {

		new Thread(){
			@Override
			public void run() {
				try {
					while(gameWindow == null){
						Thread.sleep(500);
					}
					gameWindow.anounceTurn();
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
		//		System.out.println("EnableRollButton2");
		//		if((gameWindow == null) && (gameLobbyWindow != null)){
		//			gameLobbyWindow.anounceStartGame();
		//		}
		//
		//		try {
		//			Thread.sleep(1000);
		//		} catch (final InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		if(gameWindow == null){
		//			throw new RuntimeException("No Game available.");
		//		}
		//		System.out.println("EnableRollButton1");
		//		gameWindow.anounceTurn();
	}

	synchronized public void anounceEvent(final String uri) {
		uiThreadAccess.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("fetching event: " + uri);
					final EventDTO event = template.getForObject(uri, EventDTO.class);
					System.err.println("Event for Player '" + userName + "': " + event);
					if(event.getType().equals("CreateNewGame") && (lobbyWindow != null)){
						lobbyWindow.update();
					}else if(event.getType().equals("PlayerEnterGame") && (lobbyWindow != null)){
						if((event.getPlayer() != null) && event.getPlayer().equals(userName)){
							showGameLobby(event.getResource());
						}
						else {
							lobbyWindow.update();
						}
					}else if((event.getType().equals("PlayerEnterGame") || event.getType().equals("PlayerIsReady")) && (gameLobbyWindow != null)){
						gameLobbyWindow.update();
					}
					else if((event.getType().equals("GameHasStarted")) && (gameLobbyWindow != null)){
						showGameWindow(event.getResource());
					}else if((event.getType().equals("PlayerMovedPosition")) && (gameWindow != null)){
						gameWindow.updatePositionEvent(event.getPlayer(), event.getResource());
					}else if((event.getType().equals("MoneyTransfer")) && (gameWindow != null)){
						gameWindow.updateMoney(event.getPlayer(), event.getResource());
					}else if((event.getType().equals("ChangeOwner")) && (gameWindow != null)){
						gameWindow.updatePlace(event.getPlayer(), event.getResource());
					}
				} catch (final RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


	}

	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...
			for (final Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				final NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// Iterate all IP addresses assigned to each card...
				for (final Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					final InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {
						if (inetAddr.getHostAddress().startsWith("141")) {
							// Found non-loopback site-local address. Return it immediately...
							return inetAddr;
						}
						else if ((candidateAddress == null) && (inetAddr instanceof Inet4Address)) {
							// Found non-loopback address, but not necessarily site-local.
							// Store it as a candidate to be returned if site-local address is not subsequently found...
							candidateAddress = inetAddr;
							// Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
							// only the first. For subsequent iterations, candidate will be non-null.
						}
					}
				}
			}
			if (candidateAddress != null) {
				// We did not find a site-local address, but we found some other non-loopback address.
				// Server might have a non-site-local address assigned to its NIC (or it might be running
				// IPv6 which deprecates the "site-local" concept).
				// Return this non-loopback candidate address...
				return candidateAddress;
			}
			// At this point, we did not find a non-loopback address.
			// Fall back to returning whatever InetAddress.getLocalHost() returns...
			final InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		}
		catch (final Exception e) {
			final UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}
}
