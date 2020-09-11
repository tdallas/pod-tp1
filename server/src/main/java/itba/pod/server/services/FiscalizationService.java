package itba.pod.server.services;

import itba.pod.api.interfaces.FiscalizationServiceInterface;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Table;
import itba.pod.server.elections.Election;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class FiscalizationService implements FiscalizationServiceInterface {
    private final Election election;
    private final PropertyChangeSupport pcSupport;

    public FiscalizationService(final Election election) {
        this.election = election;
        this.pcSupport  = new PropertyChangeSupport(this);
    }

    @Override
    public void register(final Table table, final Candidate fiscal) throws ElectionException {
        if (election.getStatus() == Status.INITIALIZED || election.getStatus() == Status.FINISHED) {
            // TODO: Change this to a reasonable exception
            throw new ElectionException("No new fiscal can be registered after the start of the election");
        }

        election.addFiscalToTable(table, fiscal);
    }

    @Override
    public void notifyVote(final Table table, final Candidate fiscal) {
        final String voteNotification = "New vote for " + fiscal.name() + " on polling place " + table.getId();

        pcSupport.firePropertyChange("election", null, voteNotification);
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.pcSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcSupport.removePropertyChangeListener(listener);
    }
}
