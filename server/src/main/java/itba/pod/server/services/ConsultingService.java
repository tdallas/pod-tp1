package itba.pod.server.services;

import itba.pod.api.interfaces.ConsultingServiceInterface;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Results;
import itba.pod.api.model.vote.State;
import itba.pod.api.model.vote.Table;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class ConsultingService implements ConsultingServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(ConsultingService.class);

    private Election election;

    public ConsultingService(Election election) {
        this.election = election;
    }


    @Override
    public Results getNationalResults() throws RemoteException, ElectionException {
        logger.debug("Get National Results");
        return election.getNationalResults();
    }

    @Override
    public Results getStateResults(State state) throws RemoteException, ElectionException {
        logger.debug("Get State Results {}",state);
        return election.getStateResults(state);
    }

    @Override
    public Results getTableResults(Table table) throws RemoteException, ElectionException {
        logger.debug("Get table results {}",table);
        return election.getTableResults(table);
    }


}
