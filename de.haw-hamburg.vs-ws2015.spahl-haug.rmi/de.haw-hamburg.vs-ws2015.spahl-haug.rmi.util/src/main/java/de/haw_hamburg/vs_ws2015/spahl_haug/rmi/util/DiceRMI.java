package de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DiceRMI extends Remote {
    Roll roll() throws RemoteException;
}
