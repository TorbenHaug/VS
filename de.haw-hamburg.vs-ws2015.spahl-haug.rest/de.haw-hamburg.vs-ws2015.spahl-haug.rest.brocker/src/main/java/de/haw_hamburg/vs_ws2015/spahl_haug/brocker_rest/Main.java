package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest;


import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.BrockerDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.EstatesDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Place;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.PlaceDTO;
import de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto.Player;
import de.haw_hamburg.vs_ws2015.spahl_haug.errorhandler.*;

import java.security.acl.Owner;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static String serviceId;
	private final BrockerService brockerService;


	public Main() throws Exception {
		this.brockerService = new BrockerService();
	}

	@RequestMapping(value = "/broker/{gameId}", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Brocker> getBrocker(@PathVariable(value="gameId") final String gameId) throws BrockerNotExistsException {
		final Brocker brocker = brockerService.getBrocker(gameId);
		return new ResponseEntity<Brocker>(brocker,HttpStatus.OK);
	}

	@RequestMapping(value = "/broker/{gameId}", method = RequestMethod.PUT,  produces = "application/json", consumes = "application/json")
	public ResponseEntity<Brocker> putBrocker(@PathVariable(value="gameId") final String gameId, @RequestBody final BrockerDTO brockerDTO) throws BrockerNotExistsException {
		System.err.println("Put Game: " + brockerDTO);
		try {
			return new ResponseEntity<Brocker>(brockerService.createBrocker(gameId, brockerDTO), HttpStatus.CREATED);
		} catch (final Exception e) {
			return new ResponseEntity<Brocker>(brockerService.getBrocker(gameId), HttpStatus.ALREADY_REPORTED);
		}
	}

	@RequestMapping(value = "/broker/{gameId}", method = RequestMethod.DELETE,  produces = "application/json")
	public void deleteBrocker(@PathVariable(value="gameId") final String gameId) {
		brockerService.removeBrocker(gameId);
	}

	@RequestMapping(value = "/broker/{gameId}/places", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<EstatesDTO> getPlaces(@PathVariable(value="gameId") final String gameId) throws BrockerNotExistsException {
		return new ResponseEntity<EstatesDTO>(new EstatesDTO(brockerService.getAllPlaces(gameId), gameId), HttpStatus.OK);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<PlaceDTO> getPlace(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) throws BrockerNotExistsException, PlaceNotFoundException {
		return new ResponseEntity<>(brockerService.getPlaceDTO(gameId, placeid),HttpStatus.OK);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}", method = RequestMethod.PUT,  produces = "application/json")
	public void putPlace(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid, @RequestBody final Place place) throws PlaceAlreadyExistsExeption, BrockerNotExistsException {
		brockerService.createPlace(gameId, placeid, place);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}/owner", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Player> getOwner(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) throws PlaceNotFoundException, BrockerNotExistsException, NotSoldException, PlayerDoesntExistsException {
		return new ResponseEntity<>(brockerService.getOwner(gameId,placeid), HttpStatus.OK);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}/owner", method = RequestMethod.PUT,  produces = "application/json")
	public void putOwner(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid, @RequestBody final Player player) throws PlaceNotFoundException, PlayerDoesntExistsException, BrockerNotExistsException, NotForSaleException {
		brockerService.changeOwner(gameId,placeid,player);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}/owner", method = RequestMethod.POST,  produces = "application/json")
	public void postOwner(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid, @RequestBody final Player player) throws BrockerNotExistsException, PlaceNotFoundException, PlayerDoesntExistsException, BankRejectedException, NotForSaleException, RestClientException, RepositoryException {
		System.err.println("post Owner " + player.getId());
		brockerService.buyPlace(gameId,placeid,player);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}/hypothecarycredit", method = RequestMethod.PUT,  produces = "application/json")
	public void putCredit(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.hypothecaryCredit(gameId,placeid);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}/hypothecarycredit", method = RequestMethod.DELETE,  produces = "application/json")
	public void deleteCredit(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid) {
		brockerService.deleteHypothecaryCredit(gameId,placeid);
	}

	@RequestMapping(value = "/broker/{gameId}/places/{placeid}/visit/{playerid}", method = RequestMethod.POST,  produces = "application/json")
	public void visit(@PathVariable(value="gameId") final String gameId, @PathVariable(value="placeid") final String placeid, @PathVariable(value="playerid") final String playerid) throws BrockerNotExistsException, PlaceNotFoundException, PlayerDoesntExistsException, BankRejectedException, RestClientException, RepositoryException {
		System.err.println("Visit Game: " + gameId + " placeid: " + placeid + " player: " + playerid);
		brockerService.visit(gameId,placeid,playerid);
	}

	public static void main(final String[] args) throws Exception {
		//		final ServiceRepository repo = new ServiceRepository();
		//		System.err.println(repo.getService("spahl_haug_games"));
		SpringApplication.run(Main.class, args);
	}

	public static void setServiceID(final String serviceId) {
		System.out.println("Registred as " + serviceId);
		Main.serviceId = serviceId;

	}

	public static String getServiceID() {
		return Main.serviceId;
	}


}
