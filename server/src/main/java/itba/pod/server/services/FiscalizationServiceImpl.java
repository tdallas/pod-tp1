package itba.pod.server.services;

import itba.pod.api.interfaces.FiscalizationService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Fiscal;
import itba.pod.api.model.vote.Table;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FiscalizationServiceImpl implements FiscalizationService {
    private static final Logger logger = LoggerFactory.getLogger(FiscalizationServiceImpl.class);
    private final Election election;

    public FiscalizationServiceImpl(final Election election) {
        this.election = election;
    }

    @Override
    public String register(final long tableId, final Fiscal fiscal)
            throws ElectionException {
        if (election.getStatus() == Status.INITIALIZED || election.getStatus() == Status.FINISHED)
            throw new ElectionException("No new fiscal can be registered after the start of the election");

        if (!election.getTables().containsKey(tableId)) {
            election.getTables().put(tableId, new Table(tableId));
        }

        if (election.getTable(tableId).registerFiscal(fiscal)) {
            return "Fiscal of " + fiscal.getParty().getName() + " registered on polling place " + tableId;
        } else {
            return "There is already a registered fiscal of " + fiscal.getParty().getName() + " on polling place " + tableId;
        }
    }
}
