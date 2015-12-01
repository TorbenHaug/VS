package de.haw_hamburg.vs_ws2015.spahl_haug.boards_rest.dto;

public class RollDTO {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "RollDTO{" +
                "number=" + number +
                '}';
    }
}
