package itba.pod.api.interfaces;

import itba.pod.api.model.vote.Vote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VotingServiceInterface extends Remote {

    void emitVote(Vote vote) throws RemoteException;
}
