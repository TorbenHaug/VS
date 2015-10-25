package de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util;

import java.io.Serializable;

public class Roll implements Serializable {
    private static final long serialVersionUID = 1337L;
    private int number;

    public Roll(int number){
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }
}
