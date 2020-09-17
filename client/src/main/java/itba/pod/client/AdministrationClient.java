package itba.pod.client;

import itba.pod.api.interfaces.AdministrationService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static java.lang.System.exit;

public class AdministrationClient {

    private final static String STATE = "state";
    private final static String OPEN = "open";
    private final static String CLOSE = "close";
    private static final Logger logger = LoggerFactory.getLogger(AdministrationClient.class);


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
                    electionStatus = administrationService.closeElection();
                } else {
                    throw new IllegalArgumentException("Invalid params");
                }
            }
            System.out.println(electionStatus);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            logger.info("RMI failure while requesting the administration service: " + e);
        } catch (ElectionException e) {
            logger.info(e.toString());
        }

    }
}
