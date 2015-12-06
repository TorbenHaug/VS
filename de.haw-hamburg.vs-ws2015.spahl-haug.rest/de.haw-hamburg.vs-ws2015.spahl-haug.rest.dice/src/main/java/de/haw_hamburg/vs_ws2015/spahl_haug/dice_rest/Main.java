package de.haw_hamburg.vs_ws2015.spahl_haug.dice_rest;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan
@EnableWebMvc
@RestController
@RequestMapping("/")
@EnableAutoConfiguration
public class Main {

	private static String serviceId;

	@RequestMapping(value = "/dice", method = RequestMethod.GET,  produces = "application/json")
	public Roll dice() {
		System.out.println("Anfrage");
		return new Roll();
	}

	public static void main(final String[] args) throws Exception {
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
