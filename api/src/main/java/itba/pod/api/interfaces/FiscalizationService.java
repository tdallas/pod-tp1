package itba.pod.api.interfaces;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Table;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalizationService extends Remote {
    void register(final long tableId, final Candidate fiscal) throws RemoteException, ElectionException;

    void notifyVote(final long tableId, final Candidate fiscal) throws RemoteException;
}
