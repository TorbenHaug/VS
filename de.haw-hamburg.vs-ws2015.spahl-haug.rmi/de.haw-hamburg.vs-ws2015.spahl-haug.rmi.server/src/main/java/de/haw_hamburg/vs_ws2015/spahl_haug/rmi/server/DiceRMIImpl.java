package de.haw_hamburg.vs_ws2015.spahl_haug.rmi.server;

import de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util.DiceRMI;
import de.haw_hamburg.vs_ws2015.spahl_haug.rmi.util.Roll;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DiceRMIImpl extends UnicastRemoteObject implements DiceRMI {

    /**
	 * 
	 */
	private static final long serialVersionUID = 260521651481912040L;

	protected DiceRMIImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
    public Roll roll() throws RemoteException {
        return new Roll((int)(Math.round(Math.random() * 5 ) + 1));
    }
}
