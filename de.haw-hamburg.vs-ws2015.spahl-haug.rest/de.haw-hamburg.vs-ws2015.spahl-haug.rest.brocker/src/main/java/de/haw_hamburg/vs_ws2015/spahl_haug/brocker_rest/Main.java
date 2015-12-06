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

	private final BrockerService brockerService;


	public Main() {
		this.brockerService = new BrockerService();
	}

	@RequestMapping(value = "/brocker/{gameId}", method = RequestMethod.GET,  produces = "application/json")
	public void getBrocker(@PathVariable(value="gameId") final String gameId) {
		brockerService.getBrocker(gameId);
	}

	@RequestMapping(value = "/brocker/{gameId}", method = RequestMethod.PUT,  produces = "application/json")
	public void putBrocker(@PathVariable(value="gameId") final String gameId) {
		brockerService.createBrocker(gameId);
	}

	@RequestMapping(value = "/brocker/{gameId}", method = RequestMethod.DELETE,  produces = "application/json")
	public void deleteBrocker(@PathVariable(value="gameId") final String gameId) {
		brockerService.removeBrocker(gameId);
	}

	@RequestMapping(value = "/brocker/{gameId}/places", method = RequestMethod.GET,  produces = "application/json")
	public void getPlaces(@PathVariable(value="gameId") final String gameId) {
		brockerService.getAllPlaces(gameId);
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}", method = RequestMethod.GET,  produces = "application/json")
	public void getPlace(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.getPlace(gameId, placeid);
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}", method = RequestMethod.PUT,  produces = "application/json")
	public void putPlace(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.createPlace(gameId, placeid);
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/owner", method = RequestMethod.GET,  produces = "application/json")
	public void getOwner(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.getOwner(gameId,placeid);
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/owner", method = RequestMethod.PUT,  produces = "application/json")
	public void putOwner(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.changeOwner(gameId,placeid);
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/owner", method = RequestMethod.POST,  produces = "application/json")
	public void postOwner(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.buyPlace(gameId,placeid);
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/hypothecarycredit", method = RequestMethod.PUT,  produces = "application/json")
	public void putCredit(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.hypothecaryCredit(gameId,placeid);
	}

	@RequestMapping(value = "/brocker/{gameId}/places/{placeid}/hypothecarycredit", method = RequestMethod.DELETE,  produces = "application/json")
	public void deleteCredit(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.deleteHypothecaryCredit(gameId,placeid);
	}


	public static void main(final String[] args) throws Exception {
		//		final ServiceRepository repo = new ServiceRepository();
		//		System.err.println(repo.getService("spahl_haug_games"));
		SpringApplication.run(Main.class, args);
	}


}
