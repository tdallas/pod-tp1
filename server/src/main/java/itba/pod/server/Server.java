package itba.pod.server;

import itba.pod.api.interfaces.AdministrationService;
import itba.pod.api.interfaces.ConsultingService;
import itba.pod.api.interfaces.FiscalizationService;
import itba.pod.api.interfaces.VotingService;
import itba.pod.server.elections.Election;
import itba.pod.server.services.AdministrationServiceImpl;
import itba.pod.server.services.ConsultingServiceImpl;
import itba.pod.server.services.FiscalizationServiceImpl;
import itba.pod.server.services.VotingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int ADMIN_SERVICE_PORT = 10001;
    private static final int VOTING_SERVICE_PORT = 10002;
    private static final int CONSULTING_SERVICE_PORT = 10003;
    private static final int FISCALIZATION_SERVICE_PORT = 10004;

    public static void main(String[] args) throws RemoteException {
        logger.info("Server Starting ...");
        registerServices();
    }

    /**
     * This function register every needed service.
     */
    private static void registerServices() {
        final Election election = new Election();

        final AdministrationService administrationService = new AdministrationServiceImpl(election);
        final VotingService votingService = new VotingServiceImpl(election);
        final ConsultingService consultingService = new ConsultingServiceImpl(election);
        final FiscalizationService fiscalizationService = new FiscalizationServiceImpl(election);

        try {
            final Registry registry = LocateRegistry.getRegistry();
            final Remote remoteAdministration = UnicastRemoteObject.exportObject(administrationService, 0);
            final Remote remoteConsulting = UnicastRemoteObject.exportObject(consultingService,0);
            final Remote remoteVoting = UnicastRemoteObject.exportObject(votingService,0);
            final Remote remoteFiscalization = UnicastRemoteObject.exportObject(fiscalizationService,0);

            registry.rebind("administration-service", remoteAdministration);
            logger.info("Administration service bound");

            registry.rebind("voting-service", remoteVoting);
            logger.info("Voting service bound");

            registry.rebind("consulting-service", remoteConsulting);
            logger.info("Consulting service bound");

            registry.rebind("fiscalization-service", remoteFiscalization);
            logger.info("Fiscalization service bound");

        } catch (RemoteException e) {
            logger.error(e.toString());
            logger.error("Remote exception");
        }
    }
}
