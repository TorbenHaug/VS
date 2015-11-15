package de.haw_hamburg.vs_ws2015.spahl_haug.games_rest;

public class Player {
	private final long id;
	private final String name;
	private int position = 0;
	private String place = null;
	private boolean ready;

	public Player(final String name, final long id){
		this.name = name;
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(final boolean b) {
		this.ready = b;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(final String place) {
		this.place = place;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Player other = (Player) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}



}
