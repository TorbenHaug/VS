package de.haw_hamburg.vs_ws2015.spahl_haug.rmi.server;

import de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util.DiceRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    static public void main(String[] args) throws RemoteException{
        System.out.println("hello i am the server");

        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

        DiceRMI diceRMI = new DiceRMIImpl();
        DiceRMI stub = (DiceRMI) UnicastRemoteObject.exportObject(diceRMI, 0);
        RemoteServer.setLog(System.out);

        Registry registry = LocateRegistry.getRegistry();
        registry.rebind( "DiceRMI", stub );
//        registry.rebind( "192.168.99.101:1234/DiceRMI", stub );
        System.out.println( "DiceRMI angemeldet" );
    }
}
