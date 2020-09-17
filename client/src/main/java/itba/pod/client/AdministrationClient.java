package itba.pod.client;

import itba.pod.api.interfaces.AdministrationService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import static java.lang.System.exit;

public class AdministrationClient {

    private final static String STATE = "state";
    private final static String OPEN = "open";
    private final static String CLOSE = "close";

    public static void main(final String[] args) {
        String serverAddress = System.getProperty("serverAddress");
        String actionName = System.getProperty("action");
        try {
            final AdministrationService administrationService =
                    (AdministrationService) Naming.lookup("//" + serverAddress + "/administration-service");
            Status electionStatus;
            if (actionName.equals(STATE)) {
                electionStatus = administrationService.consultElectionStatus();
            } else {
                if (actionName.equals(OPEN)) {
                    electionStatus = administrationService.openElections();
                } else if (actionName.equals(CLOSE)) {
                    electionStatus = administrationService.finishElections();
                } else {
                    throw new IllegalArgumentException("Invalid params");
                }
            }
            System.out.println(electionStatus);
        } catch (NotBoundException | IOException | ElectionException e) {
            // FIXME
            System.out.println(e.getMessage());
            exit(-1);
        }
    }
}
