package itba.pod.server.services;

import itba.pod.api.interfaces.VotingService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Vote;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VotingServiceImpl implements VotingService {
    private static final Logger logger = LoggerFactory.getLogger(VotingServiceImpl.class);
    private final Election election;

    public VotingServiceImpl(Election election) {
        this.election = election;
    }

    @Override
    public void emitVote(Vote vote) throws ElectionException {
        election.emitVote(vote);
        logger.info("Vote emitted for " + vote.toString());
    }
}
