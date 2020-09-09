package itba.pod.api.interfaces;

import itba.pod.api.model.election.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdministrationServiceInterface extends Remote {

    void openElections() throws RemoteException;

    Status consultElectionStatus();

    void finishElections() throws RemoteException;
}
