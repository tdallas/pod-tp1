package itba.pod.server.services;

import itba.pod.api.interfaces.AdministrationService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class AdministrationServiceImpl implements AdministrationService {
    private static final Logger logger = LoggerFactory.getLogger(AdministrationServiceImpl.class);
    private Election election;

    public AdministrationServiceImpl(Election election) {
        this.election = election;
    }

    @Override
    public Status openElections() throws RemoteException, ElectionException {
        logger.info("Elections opened");
        return election.setStatus(Status.INITIALIZED);
    }

    @Override
    public Status consultElectionStatus() {
        return election.getStatus();
    }

    @Override
    public Status finishElections() throws RemoteException, ElectionException {
        logger.info("Elections closed");
        return election.setStatus(Status.FINISHED);
    }
}
