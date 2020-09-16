package itba.pod.server.services;

import itba.pod.api.interfaces.ConsultingService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Results;
import itba.pod.api.model.vote.State;
import itba.pod.api.model.vote.Table;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class ConsultingServiceImpl implements ConsultingService {
    private static final Logger logger = LoggerFactory.getLogger(ConsultingServiceImpl.class);
    private final Election election;

    public ConsultingServiceImpl(Election election) {
        this.election = election;
    }


    @Override
    public Results getNationalResults() throws ElectionException {
        logger.debug("Get National Results");
        return election.getNationalResults();
    }

    @Override
    public Results getStateResults(State state) throws ElectionException {
        logger.debug("Get State Results {}",state);
        return election.getStateResults(state);
    }

    @Override
    public Results getTableResults(Table table) throws ElectionException {
        logger.debug("Get table results {}",table);
        return election.getTableResults(table);
    }


}
