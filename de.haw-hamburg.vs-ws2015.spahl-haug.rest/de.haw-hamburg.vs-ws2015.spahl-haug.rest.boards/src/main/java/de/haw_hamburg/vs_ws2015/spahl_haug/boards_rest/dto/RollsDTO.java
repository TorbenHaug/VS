package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

public class RollsDTO {
    private RollDTO roll1;
    private RollDTO roll2;

    public RollDTO getRoll1() {
        return roll1;
    }

    public void setRoll1(RollDTO roll1) {
        this.roll1 = roll1;
    }

    public RollDTO getRoll2() {
        return roll2;
    }

    public void setRoll2(RollDTO roll2) {
        this.roll2 = roll2;
    }

    @Override
    public String toString() {
        return "RollsDTO{" +
                "roll1=" + roll1 +
                ", roll2=" + roll2 +
                '}';
    }
}
