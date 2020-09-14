package itba.pod.server.services;

import itba.pod.api.interfaces.FiscalizationService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Table;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class FiscalizationServiceImpl implements FiscalizationService {
    private final Election election;
    private final PropertyChangeSupport pcSupport;
    private static final Logger logger = LoggerFactory.getLogger(FiscalizationServiceImpl.class);

    public FiscalizationServiceImpl(final Election election) {
        this.election = election;
        this.pcSupport  = new PropertyChangeSupport(this);
    }

    @Override
    public String register(final long tableId, final Candidate fiscal) throws ElectionException {
        if (election.getStatus() == Status.INITIALIZED || election.getStatus() == Status.FINISHED)
            throw new ElectionException("No new fiscal can be registered after the start of the election");

        System.out.println("HOLA");
        logger.info("HOLA");

        if (!election.getTables().containsKey(tableId)) {
            election.getTables().put(tableId, new Table(tableId));
            logger.info("mesa " + tableId);
        }

        if (election.getTable(tableId).registerFiscal(fiscal))
            return "Fiscal of " + fiscal.getName() + " registered on polling place " + tableId;
        else
            return "There is already a registered fiscal of " + fiscal.getName() + " on polling place " + tableId;
    }

    @Override
    // TODO: Change this to listen for changes in the election instance
    public void notifyVote(final long tableId, final Candidate fiscal) {
        if (election.getTable(tableId).hasRegisteredFiscal(fiscal)) {
            final String voteNotification = "New vote for " + fiscal.getName() + " on polling place " + tableId;

            // TODO: Change this to avoid firing (making oldValue = newValue) unless the table has notified a new vote.
            pcSupport.firePropertyChange("election", null, voteNotification);
        }
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.pcSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcSupport.removePropertyChangeListener(listener);
    }
}
