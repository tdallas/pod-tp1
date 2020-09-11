package itba.pod.server.services;

import itba.pod.api.interfaces.AdministrationService;
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
    public void openElections() throws RemoteException {
        election.setStatus(Status.INITIALIZED);
        logger.info("Elections opened");
    }

    @Override
    public Status consultElectionStatus() {
        return election.getStatus();
    }

    @Override
    public void finishElections() throws RemoteException {
        election.setStatus(Status.FINISHED);
        logger.info("Elections closed");
    }
}
