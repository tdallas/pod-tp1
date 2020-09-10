package itba.pod.server.elections;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.State;
import itba.pod.api.model.vote.Table;
import itba.pod.api.model.vote.Vote;
import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.SPAV;
import itba.pod.server.votingSystems.STAR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Election {
    private static Logger logger = LoggerFactory.getLogger(Election.class);

    private Status status;
    private List<Vote> votes;

    public Election() {
        status = Status.NOT_INITIALIZED;
        votes = new LinkedList<>();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        // TODO
    }

    public void emitVote(Vote vote) {
        // TODO
    }

    public Results getNationalResults() throws RemoteException, ElectionException {
        if (status == Status.NOT_INITIALIZED) {
            throw new ElectionException("Election not initialize");
        }
        if (status == Status.INITIALIZED) {
            //parciales
            FPTP f = new FPTP(votes);
            return new Results(status, f.calculateScore());
        } else {
            STAR s = new STAR(votes);
            return new Results(status, s.calculateScore());
        }
    }

    public Results getStateResults(State state) throws RemoteException, ElectionException {
        if (status == Status.NOT_INITIALIZED) {
            throw new ElectionException("Election not initialize");
        }
        //TODO filter votes (crear una nueva lista de votos solo con los votes de ese state
        List<Vote> filterStateVotes = new LinkedList<>();
        if (status == Status.INITIALIZED) {
            //parciales
            FPTP f = new FPTP(filterStateVotes);
            return new Results(status, f.calculateScore());
        } else {
            SPAV s = new SPAV(filterStateVotes);
            return new Results(status, s.calculateScore());
        }

    }

    public Results getTableResults(Table table) throws RemoteException, ElectionException {
        if (status == Status.NOT_INITIALIZED) {
            throw new ElectionException("Election not initialize");
        }
        //TODO filter votes (crear una nueva lista de votos solo con la mesa)
        List<Vote> filterStateVotes = new LinkedList<>();
        FPTP f = new FPTP(filterStateVotes);
        return new Results(status, f.calculateScore());

    }
}
