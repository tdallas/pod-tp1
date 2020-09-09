package itba.pod.server.services;

import itba.pod.api.interfaces.VotingServiceInterface;
import itba.pod.api.model.vote.Vote;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class VotingService implements VotingServiceInterface {
    private static Logger logger = LoggerFactory.getLogger(VotingService.class);
    private Election election;

    public VotingService(Election election) {
        this.election = election;
    }

    @Override
    public void emitVote(Vote vote) throws RemoteException {
        logger.debug("Emitting " + vote.toString());
        election.emitVote(vote);
    }
}
