package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest;


public enum Place {
	GO(0),
	OLDKENTROAD(1),
	COMMUNITIYCHEST1(2),
	WHITECHAPPELROAD(3);

	private int position;

	Place(final int id){
		this.position = id;
	}

	public int getPosition() {
		return this.position;
	}
}
