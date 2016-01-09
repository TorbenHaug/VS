package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository.RegisterServiceDTO;

@Component
public class ApplicationClose implements ApplicationListener<ContextClosedEvent>{
	@Override
	public void onApplicationEvent(final ContextClosedEvent arg0) {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		final String base64Creds = "YWJxMzI5OkRLR1JIZDIwMTUy";
		headers.add("Authorization", "Basic " + base64Creds);
		final HttpEntity<String> request = new HttpEntity<String>(headers);
		restTemplate.exchange("https://vs-docker.informatik.haw-hamburg.de/ports/8053" + Main.getServiceID(), HttpMethod.DELETE, request,String.class);

	}
}



