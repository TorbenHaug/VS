package de.haw_hamburg.vs_ws2015.spahl_haug.brocker_rest.dto;

import java.util.ArrayList;
import java.util.List;

public class EstatesDTO {
	private List<String> estates;

	public EstatesDTO() {}

	public EstatesDTO(final List<Place> estatesIn, final String brockerServiceUri) {
		this.estates = new ArrayList<>();
		for(int i= 0; i< estatesIn.size(); i++ ){
			this.estates.add(brockerServiceUri + "/places/" + i);
		}
	}

	public List<String> getEstates() {
		return estates;
	}


}
