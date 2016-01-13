package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto;

import java.util.List;

public class PlaceDTO extends Place {

	private final String place;
	private final String owner;
	private final int value;
	private final List<Integer> rent;
	private final List<Integer> cost;
	private final int houses;
	private final String visit;

	public PlaceDTO(final String place, final String owner, final int value, final List<Integer> rent, final List<Integer> cost, final int houses,
			final String visit) {
		this.place = place;
		this.owner = owner;
		this.value = value;
		this.rent = rent;
		this.cost = cost;
		this.houses = houses;
		this.visit = visit;
	}

	@Override
	public String getPlace() {
		return place;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public List<Integer> getRent() {
		return rent;
	}

	@Override
	public List<Integer> getCost() {
		return cost;
	}

	@Override
	public int getHouses() {
		return houses;
	}

	public String getVisit() {
		return visit;
	}



}
