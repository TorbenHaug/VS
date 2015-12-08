package de.haw_hamburg.vs_ws2015.spahl_haug.events_rest;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationClose implements ApplicationListener<ContextClosedEvent>{
	@Override
	public void onApplicationEvent(final ContextClosedEvent arg0) {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete("https://vs-docker.informatik.haw-hamburg.de/ports/8053" + Main.getServiceID());

	}
}



