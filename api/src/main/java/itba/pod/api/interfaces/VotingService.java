package itba.pod.api.interfaces;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Vote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VotingService extends Remote {
    void emitVote(Vote vote) throws RemoteException, ElectionException;
}
