package de.haw_hamburg.vs_ws2015.spahl_haug.rmi.client;

import de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util.DiceRMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    static public void main(String[] args)  throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        DiceRMI diceRMI = (DiceRMI) registry.lookup( "DiceRMI" );
        System.out.println(diceRMI.roll().getNumber());
//        for(int i= 0; i<25; i++){
//            System.out.println(diceRMI.roll().getNumber());
//        }
    }
}
