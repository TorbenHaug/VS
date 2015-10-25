package de.haw_hamburg.vs_ws2015.spahl_haug.rmi.server;

import de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util.DiceRMI;
import de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util.Roll;

import java.rmi.RemoteException;

public class DiceRMIImpl implements DiceRMI {

    @Override
    public Roll roll() throws RemoteException {
        return new Roll((int)(Math.round(Math.random() * 5 ) + 1));
    }
}
