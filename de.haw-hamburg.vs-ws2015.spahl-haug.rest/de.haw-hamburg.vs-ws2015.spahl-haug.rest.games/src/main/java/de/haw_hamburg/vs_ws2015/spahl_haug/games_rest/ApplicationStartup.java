package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.RegisterServiceDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.games_rest.dto.ResponseRegisterServiceDTO;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent arg0) {
		final RegisterServiceDTO dto = new RegisterServiceDTO();
		dto.setName("spahl_haug_games");
		dto.setDescription("GamesService von Louisa Spahl und Torben Haug");
		dto.setService("games");
		try {
			dto.setUri("http://" + InetAddress.getLocalHost().getHostAddress() + "/games");
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<ResponseRegisterServiceDTO> registerServiceDTO = restTemplate.postForEntity("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services/", dto, ResponseRegisterServiceDTO.class);
		Main.setServiceID(registerServiceDTO.getBody().get_uri());
		System.err.println(registerServiceDTO.getBody().get_uri());
	}

}
