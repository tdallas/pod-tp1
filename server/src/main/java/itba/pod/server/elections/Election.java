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

    // Only for testing purposes
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
        final Status currentStatus = this.getStatus();

        if (newStatus.equals(currentStatus)) {
            return currentStatus;
        } else if (this.isStartingElection(newStatus)) {
            return changeElectionStatus(newStatus);
        } else if (this.isClosingElection(newStatus)) {
            tables.values().parallelStream().forEach(Table::close);

            return changeElectionStatus(newStatus);
        } else {
            throw new ElectionException("The election status cannot be forcefully changed from " + this.status +
                    " to " + newStatus);
        }
    }

    private Status changeElectionStatus(final Status status) {
        writeLock.lock();
        this.status = status;
        writeLock.unlock();
        return getStatus();
    }

    public void emitVote(final Vote vote) throws ElectionException {
        if (status.equals(Status.NOT_INITIALIZED) || status.equals(Status.FINISHED))
            throw new ElectionException("Election does not admit votes. Status is currently: " + status.toString());

        addVote(vote);
    }

    private void addVote(final Vote vote) {
        writeLock.lock();
        this.votes.add(vote);
        this.notifyVote(vote.getTable().getId(), vote.getFPTPCandidate().getParty());
        writeLock.unlock();
        logger.info("Now there " + (votes.size() == 1 ? "is 1 vote" : ("are " + votes.size() + " votes")));
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
            List<Vote> n = new LinkedList<>();
            for(Vote v:votes){
                n.add(v.clone());
            }
            for(Vote v:n){
                v.setTickets(new LinkedList<>(v.getTickets()).subList(0,v.getTickets().size()-1));
            }

            STAR s = new STAR(n);
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
                .filter(v -> v.getState().equals(state))
                .collect(Collectors.toList());
        if(filterStateVotes.size()==0){
            readLock.unlock();
            throw new ElectionException("No votes registered for "+state.toString());
        }
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
                .filter(v -> v.getTable().equals(table))
                .collect(Collectors.toList());
        if(filterStateVotes.size()==0){
            readLock.unlock();
            throw new ElectionException("No votes registered for "+table.toString());
        }
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

    private void notifyVote(final long tableId, final Party party) {
        if (!this.tables.containsKey(tableId))
            tables.put(tableId, new Table(tableId));

        final Table table = this.tables.get(tableId);

        if (table.hasRegisteredFiscalFor(party)) {
            final String newVote = "New vote for " + party + " on polling place " + tableId;

            try {
                table.getFiscalOfParty(party).notifyVote(newVote);
            } catch (ElectionException e) {
                logger.info(e.toString());
            } catch (RemoteException e) {
                logger.info("RMI failure while requesting the fiscalization subscription for fiscal of party " +
                        party + " in table " + tableId);
            }
        }
    }

    public boolean hasStarted() {
        Status currentStatus = this.getStatus();
        return currentStatus == Status.INITIALIZED || currentStatus == Status.FINISHED;
    }

    public void registerFiscal(final long tableId, final Fiscal fiscal) throws ElectionException {
        if (hasStarted())
            throw new ElectionException("No new fiscal can be registered after the start of the election");

        writeLock.lock();

        if (!getTables().containsKey(tableId))
            getTables().put(tableId, new Table(tableId));

        getTable(tableId).registerFiscal(fiscal);
        writeLock.unlock();
    }

    private boolean isStartingElection(final Status newStatus) {
        return this.isStatusTransitioning(Status.NOT_INITIALIZED, Status.INITIALIZED, newStatus);
    }

    private boolean isClosingElection(final Status newStatus) {
        return this.isStatusTransitioning(Status.INITIALIZED, Status.FINISHED, newStatus);
    }

    private boolean isStatusTransitioning(final Status from, final Status to, final Status status) {
        return this.getStatus().equals(from) && status.equals(to);
    }
}
