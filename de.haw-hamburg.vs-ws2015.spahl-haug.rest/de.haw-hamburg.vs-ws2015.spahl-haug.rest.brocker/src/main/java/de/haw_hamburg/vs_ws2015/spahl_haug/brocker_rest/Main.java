package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;

import java.util.ArrayList;
import java.util.List;

import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.*;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.net.ssl.SSLEngineResult;


@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	@RequestMapping(value = "/brocker/{gameId}", method = RequestMethod.GET,  produces = "application/json")
	public void getBrocker() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}", method = RequestMethod.PUT,  produces = "application/json")
	public void putBrocker() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}", method = RequestMethod.DELETE,  produces = "application/json")
	public void deleteBrocker() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places", method = RequestMethod.GET,  produces = "application/json")
	public void getPlaces() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}", method = RequestMethod.GET,  produces = "application/json")
	public void getPlace() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}", method = RequestMethod.PUT,  produces = "application/json")
	public void putPlace() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/owner", method = RequestMethod.GET,  produces = "application/json")
	public void getOwner() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/owner", method = RequestMethod.PUT,  produces = "application/json")
	public void putOwner() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/owner", method = RequestMethod.POST,  produces = "application/json")
	public void postOwner() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/hypothecarycredit", method = RequestMethod.PUT,  produces = "application/json")
	public void putCredit() {
		throw new RuntimeException("Not Yet Implemented");
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/hypothecarycredit", method = RequestMethod.DELETE,  produces = "application/json")
	public void deleteCredit() {
		throw new RuntimeException("Not Yet Implemented");
	}


	public static void main(final String[] args) throws Exception {
		//		final ServiceRepository repo = new ServiceRepository();
		//		System.err.println(repo.getService("spahl_haug_games"));
		SpringApplication.run(Main.class, args);
	}


}
