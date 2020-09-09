package itba.pod.server.services;

import itba.pod.api.election.Status;
import itba.pod.server.elections.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;

public class AdministrationService implements Remote {
    private static final Logger logger = LoggerFactory.getLogger(AdministrationService.class);
    private Election election;

    public AdministrationService(Election election) {
        this.election = election;
    }

    public void openElections() {
        election.setStatus(Status.INITIALIZED);
        logger.info("Elections opened");
    }

    public Status consultElectionStatus() {
        return election.getStatus();
    }

    public void finishElections() {
        election.setStatus(Status.FINISHED);
        logger.info("Elections closed");
    }
}
