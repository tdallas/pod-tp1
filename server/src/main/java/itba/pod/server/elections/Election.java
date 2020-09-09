package itba.pod.server.elections;

import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Election {
    private static Logger logger = LoggerFactory.getLogger(Election.class);

    private Status status;
    private List<Vote> votes;

    public Election() {
        status = Status.NOT_INITIALIZED;
        votes = new ArrayList<>();
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) {
        // TODO
    }

    public void emitVote(Vote vote) {
        // TODO
    }
}
