package itba.pod.server.elections;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.*;
import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.SPAV;
import itba.pod.server.votingSystems.STAR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.*;

public class Election {
    private static final Logger logger = LoggerFactory.getLogger(Election.class);

    private final ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = reentrantLock.readLock();
    private final Lock writeLock = reentrantLock.writeLock();

    private Status status;
    private final List<Vote> votes;
    private final Map<Long, Table> tables;

    public Election() {
        this.status = Status.NOT_INITIALIZED;
        this.votes = new LinkedList<>();
        this.tables = new HashMap<>();
    }

    // TODO: Check if it makes sense to keep this constructor just for stubs while testing
    public Election(final Status newStatus) {
        this.status = newStatus;
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
    public Status setStatus(final Status newStatus) throws ElectionException {
        if (this.status.equals(newStatus)) {
            // FIXME use custom exception pls
            throw new ElectionException("Invalid status to set");
        } else if (this.status.equals(Status.INITIALIZED) && newStatus.equals(Status.FINISHED)) {
            // TODO calculate results etc etc
            tables.values().parallelStream().forEach(Table::close);

            return changeElectionStatus(newStatus);
        } else if (this.status.equals(Status.NOT_INITIALIZED) && newStatus.equals(Status.INITIALIZED)) {
            // Does this imply something more than only changing status?
            return changeElectionStatus(newStatus);
        } else {
            // FIXME use custom exception pls
            throw new ElectionException("The election status cannot be forcefully changed from " + this.status +
                    " to " + newStatus);
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

    public void emitVote(final Vote vote) throws ElectionException, RemoteException {
        if (status.equals(Status.NOT_INITIALIZED) || status.equals(Status.FINISHED)) {
            throw new ElectionException("Election does not admit votes. Status is currently: " + status.toString());
        }
        addVote(vote);
    }

    private void addVote(final Vote vote) throws RemoteException {
        writeLock.lock();
        this.votes.add(vote);
        this.notifyVote(vote.getTable().getId(), vote.getFPTPCandidate().getParty());
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

    private void notifyVote(final long tableId, final Party party) throws RemoteException {
        if (!this.tables.containsKey(tableId))
            tables.put(tableId, new Table(tableId));

        final Table table = this.tables.get(tableId);

        if (table.hasRegisteredFiscalFor(party)) {
            final String newVote = "New vote for " + party + " on polling place " + tableId;

            table.getFiscalOfParty(party).notifyVote(newVote);
        }
    }
}
