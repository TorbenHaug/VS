package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.model;

public class PostRollDTO {

	private RollDTO roll1;
	private RollDTO roll2;

	public PostRollDTO() {}

	public PostRollDTO(final RollDTO roll1, final RollDTO roll2) {
		this.roll1 = roll1;
		this.roll2 = roll2;

	}

	public RollDTO getRoll1() {
		return roll1;
	}

	public void setRoll1(final RollDTO roll1) {
		this.roll1 = roll1;
	}

	public RollDTO getRoll2() {
		return roll2;
	}

	public void setRoll2(final RollDTO roll2) {
		this.roll2 = roll2;
	}

	@Override
	public String toString() {
		return "PostPostRollDTO [roll1=" + roll1 + ", roll2=" + roll2 + "]";
	}



}
