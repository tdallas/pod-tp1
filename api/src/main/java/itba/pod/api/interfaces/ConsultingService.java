package itba.pod.api.interfaces;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Results;
import itba.pod.api.model.vote.State;
import itba.pod.api.model.vote.Table;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConsultingService extends Remote {
    Results getNationalResults() throws RemoteException, ElectionException;
    Results getStateResults(State state) throws RemoteException, ElectionException;
    Results getTableResults(Table table) throws RemoteException, ElectionException;
}
