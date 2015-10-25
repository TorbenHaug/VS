package de.haw_hamburg.vs_ws2015.spahl_haug.dice_rest;

import java.util.Random;

/**
 * Created by Louisa on 25.10.2015.
 */
public class Roll {
    private int number;

    public Roll(){
        this.number = new Random(System.currentTimeMillis()).nextInt(6) + 1;
    }

    public int getNumber() {
        return this.number;
    }
}
