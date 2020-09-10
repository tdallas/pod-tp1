package itba.pod.api.interfaces;

import itba.pod.api.model.election.Results;
import itba.pod.api.model.vote.State;
import itba.pod.api.model.vote.Table;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConsultingServiceInterface extends Remote {
    Results getNationalResults() throws RemoteException;
    Results getStateResults(State state) throws RemoteException;
    Results getTableResults(Table table) throws RemoteException;
}
