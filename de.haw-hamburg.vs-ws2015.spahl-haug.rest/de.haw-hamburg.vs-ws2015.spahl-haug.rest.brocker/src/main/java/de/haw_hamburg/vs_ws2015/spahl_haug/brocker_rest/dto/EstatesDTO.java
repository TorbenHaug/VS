package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto;

import java.util.List;

public class EstatesDTO {
	private List<Place> estates;

	public EstatesDTO() {}

	public EstatesDTO(final List<Place> estates) {
		this.setEstates(estates);
	}

	public List<Place> getEstates() {
		return estates;
	}

	public void setEstates(final List<Place> estates) {
		this.estates = estates;
	}



}
