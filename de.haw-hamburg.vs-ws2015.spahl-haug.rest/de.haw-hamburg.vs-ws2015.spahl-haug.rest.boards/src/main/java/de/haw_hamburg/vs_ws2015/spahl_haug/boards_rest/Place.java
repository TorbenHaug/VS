package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;

import java.util.Arrays;
import java.util.List;

public enum Place {
	GO(0,"NotForSale"),
	MEDITERRANEANAVENUE(1, null, 60, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	COMMUNITIYCHEST1(2,"NotForSale"),
	BALTICAVENUE(3, null, 60, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	INCOMETAX(4,"NotForSale"),
	READINGRAILROAD(5, null, 200, Arrays.asList(1),Arrays.asList(),0),
	ORIENTALAVENUE(6, null, 100, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	CHANGE1(7,"NotForSale"),
	VERMONTAVENUE(8, null, 100, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	CONNECTICUTAVENUE(9, null, 120, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	INJAIL(10,"NotForSale"),
	STCHARLESPLACE(11, null, 140, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	ELECTRICCOMPANY(12, null, 150, Arrays.asList(1),Arrays.asList(),0),
	STATESAVEUE(13, null, 140, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	VIRGINIAAVENUE(14, null, 160, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	PENINSYLVANIARAILROAD(15, null, 200, Arrays.asList(1),Arrays.asList(10),0),
	STJAMESPLACE(16, null, 180, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	COMMUNITIYCHEST2(17,"NotForSale"),
	TENNESSEEAVENUE(18, null, 180, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	NEWYORKAVENUE(19, null, 200, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	FREEPARKIN(20,"NotForSale"),
	KENTUCKYAVENUE(21, null, 220, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	CHANGE2(22,"NotForSale"),
	INDIANAAVENUE(23, null, 220, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	ILLINOISAVENUE(24, null, 240, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	BANDORAILROAD(25, null, 200, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	ATLANTICAVENUE(26, null, 260, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	VENTNORAVENUE(27, null, 260, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	WATERWORKS(28, null, 150, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	MARVINGARDENS(29, null, 280, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	GOTOJAIL(30,"NotForSale"),
	PACIFICAVENUE(31, null, 300, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	NORTHCAROLINAAVENUE(32, null, 300, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	COMMUNITIYCHEST3(33,"NotForSale"),
	PENINSYLVANIAAVENUE(34, null, 320, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	SHORTLINE(35, null, 200, Arrays.asList(1),Arrays.asList(10),0),
	CHANGE3(36,"NotForSale"),
	PARKPLACE(37, null, 350, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0),
	LUXURYTAX(38,"NotForSale"),
	BOARDWALK(39, null, 350, Arrays.asList(1,2,3,4,5),Arrays.asList(10,20,30,40),0);


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
