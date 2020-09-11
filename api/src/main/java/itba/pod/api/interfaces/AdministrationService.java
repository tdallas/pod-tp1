package itba.pod.api.interfaces;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdministrationService extends Remote {

    Status openElections() throws RemoteException, ElectionException;

    Status consultElectionStatus() throws RemoteException;

    Status finishElections() throws RemoteException, ElectionException;
}
