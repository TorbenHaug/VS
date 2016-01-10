package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.List;

public enum Place {
	GO(0),
	MEDITERRANEANAVENUE(1),
	COMMUNITIYCHEST1(2),
	BALTICAVENUE(3),
	INCOMETAX(4),
	READINGRAILROAD(5),
	ORIENTALAVENUE(6),
	CHANGE1(7),
	VERMONTAVENUE(8),
	CONNECTICUTAVENUE(9),
	INJAIL(10),
	STCHARLESPLACE(11),
	ELECTRICCOMPANY(12),
	STATESAVEUE(13),
	VIRGINIAAVENUE(14),
	PENINSYLVANIARAILROAD(15),
	STJAMESPLACE(16),
	COMMUNITIYCHEST2(17),
	TENNESSEEAVENUE(18),
	NEWYORKAVENUE(19),
	FREEPARKIN(20),
	KENTUCKYAVENUE(21),
	CHANGE2(22),
	INDIANAAVENUE(23),
	ILLINOISAVENUE(24),
	BANDORAILROAD(25),
	ATLANTICAVENUE(26),
	VENTNORAVENUE(27),
	WATERWORKS(28),
	MARVINGARDENS(29),
	GOTOJAIL(30),
	PACIFICAVENUE(31),
	NORTHCAROLINAAVENUE(32),
	COMMUNITIYCHEST3(33),
	PENINSYLVANIAAVENUE(34),
	SHORTLINE(35),
	CHANGE3(36),
	PARKPLACE(37),
	LUXURYTAX(38),
	BOARDWALK(39);


	private int position;
	private String owner;
	private int value;
	private List<Integer> rent;
	private List<Integer> cost;
	private int houses;



	private Place(final int position, final String owner, final int value, final List<Integer> rent, final List<Integer> cost, final int houses) {
		this(position, owner);
		this.value = value;
		this.rent = rent;
		this.cost = cost;
		this.houses = houses;
	}

	private Place(final int position, final String owner){
		this(position);
		this.owner = owner;
	}

	private Place(final int id){
		this.position = id;
	}

	public int getPosition() {
		return this.position;
	}

	public String getOwner() {
		return owner;
	}

	public int getValue() {
		return value;
	}

	public List<Integer> getRent() {
		return rent;
	}

	public List<Integer> getCost() {
		return cost;
	}

	public int getHouses() {
		return houses;
	}


}
