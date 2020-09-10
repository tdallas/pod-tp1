package itba.pod.server.services;

import itba.pod.api.model.vote.Vote;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class VotingServiceImpl implements itba.pod.api.interfaces.VotingService {
    private static Logger logger = LoggerFactory.getLogger(VotingServiceImpl.class);
    private Election election;

    public VotingServiceImpl(Election election) {
        this.election = election;
    }

    @Override
    public void emitVote(Vote vote) throws RemoteException {
        logger.debug("Emitting " + vote.toString());
        election.emitVote(vote);
    }
}
