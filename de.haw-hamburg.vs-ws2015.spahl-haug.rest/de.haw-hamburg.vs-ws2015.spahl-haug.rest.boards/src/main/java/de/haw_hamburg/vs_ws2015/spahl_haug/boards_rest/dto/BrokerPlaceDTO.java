package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

import java.util.List;

public class BrokerPlaceDTO {
	private String place;
	private String owner;
	private int value;
	private List<Integer> rent;
	private List<Integer> cost;
	private int houses;

	public BrokerPlaceDTO() {}

	public BrokerPlaceDTO(final String place, final String owner, final int value, final List<Integer> rent, final List<Integer> cost, final int houses) {
		super();
		this.place = place;
		this.owner = owner;
		this.value = value;
		this.rent = rent;
		this.cost = cost;
		this.houses = houses;
	}



	public String getPlace() {
		return place;
	}

	public void setPlace(final String name) {
		this.place = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(final String owner) {
		this.owner = owner;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public List<Integer> getRent() {
		return rent;
	}

	public void setRent(final List<Integer> rent) {
		this.rent = rent;
	}

	public List<Integer> getCost() {
		return cost;
	}

	public void setCost(final List<Integer> cost) {
		this.cost = cost;
	}

	public int getHouses() {
		return houses;
	}

	public void setHouses(final int houses) {
		this.houses = houses;
	}



}
