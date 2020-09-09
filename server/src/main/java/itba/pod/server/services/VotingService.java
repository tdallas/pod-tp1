package itba.pod.server.services;

import itba.pod.api.vote.Vote;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;

public class VotingService implements Remote {
    private static Logger logger = LoggerFactory.getLogger(VotingService.class);
    private Election election;

    public VotingService(Election election) {
        this.election = election;
    }

    public void emitVote(Vote vote) {
        logger.debug("Emitting " + vote.toString());
        election.emitVote(vote);
    }
}
