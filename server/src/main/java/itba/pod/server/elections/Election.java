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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.*;

public class Election {
    private static final Logger logger = LoggerFactory.getLogger(Election.class);

    private final ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = reentrantLock.readLock();
    private final Lock writeLock = reentrantLock.writeLock();

    private Status status;
    private List<Vote> votes;
    private final Map<Long, Table> tables;

    public Election() {
        this.status = Status.NOT_INITIALIZED;
        this.votes = new LinkedList<>();
        this.tables = new HashMap<>();
    }

    public Status getStatus() {
        readLock.lock();
        final Status statusToReturn = status;
        readLock.unlock();
        return statusToReturn;
    }

    // setStatus(NOT_INITIALIZED) is not a valid flow so there is no need to compute that logic
    public Status setStatus(final Status status) throws ElectionException {
        if (getStatus().equals(status)) {
            // FIXME use custom exception pls
            throw new ElectionException("Invalid status to set");
        } else if (getStatus().equals(Status.INITIALIZED) && status.equals(Status.FINISHED)) {
            return changeElectionStatus(status);
            // TODO calculate results etc etc
        } else if (getStatus().equals(Status.NOT_INITIALIZED) && status.equals(Status.INITIALIZED)) {
            // Does this implies something more than only changing status?
            return changeElectionStatus(status);
        } else {
            // FIXME use custom exception pls
            throw new ElectionException("Invalid status to set");
        }
        // si el status a setear es finalizada --> calculo resultados?
        // si el status a setear es abiertas (?) --> las abro
    }

    private Status changeElectionStatus(final Status status) {
        writeLock.lock();
        this.status = status;
        writeLock.unlock();
        return getStatus();
    }

    public void emitVote(final Vote vote) throws ElectionException {
        if (status.equals(Status.NOT_INITIALIZED) || status.equals(Status.FINISHED)) {
            throw new ElectionException("Election does not admit votes. Status is currently: " + status.toString());
        }
        addVote(vote);
    }

    private void addVote(final Vote vote) {
        writeLock.lock();
        this.votes.add(vote);
        writeLock.unlock();
        logger.info("Now there are {} votes", votes.size());
    }

    public Results getNationalResults() throws ElectionException {
        if (status == Status.NOT_INITIALIZED) {
            throw new ElectionException("Election not initialized");
        }
        readLock.lock();
        if (status == Status.INITIALIZED) {
            //parciales
            FPTP f = new FPTP(votes);
            readLock.unlock();
            return new Results(status, f.calculateScore());
        } else {
            STAR s = new STAR(votes);
            readLock.unlock();
            return new Results(status, s.calculateScore());
        }
    }

    public Results getStateResults(State state) throws ElectionException {
        if (status == Status.NOT_INITIALIZED) {
            throw new ElectionException("Election not initialized");
        }
        readLock.lock();
        List<Vote> filterStateVotes = votes
                .stream()
                .filter(v->v.getState().equals(state))
                .collect(Collectors.toList());
        if (status == Status.INITIALIZED) {
            //parciales
            FPTP f = new FPTP(filterStateVotes);
            readLock.unlock();
            return new Results(status, f.calculateScore());
        } else {
            SPAV s = new SPAV(filterStateVotes);
            readLock.unlock();
            return new Results(status, s.calculateScore());
        }
    }

    public Results getTableResults(Table table) throws ElectionException {
        if (status == Status.NOT_INITIALIZED) {
            throw new ElectionException("Election not initialized");
        }
        readLock.lock();
        List<Vote> filterStateVotes = votes
                .stream()
                .filter(v->v.getTable().equals(table))
                .collect(Collectors.toList());
        FPTP f = new FPTP(filterStateVotes);
        readLock.unlock();
        return new Results(status, f.calculateScore());
    }

    public Map<Long, Table> getTables() {
        return tables;
    }

    public Table getTable(final long tableId) {
        return tables.get(tableId);
    }

    public void addTable(final long tableId) {
        this.tables.put(tableId, new Table(tableId));
    }
}
