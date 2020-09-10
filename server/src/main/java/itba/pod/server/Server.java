package itba.pod.server;

import itba.pod.server.elections.Election;
import itba.pod.server.services.AdministrationServiceImpl;
import itba.pod.server.services.ConsultingServiceImpl;
import itba.pod.server.services.FiscalizationServiceImpl;
import itba.pod.server.services.VotingServiceImpl;
import itba.pod.api.interfaces.AdministrationService;
import itba.pod.api.interfaces.ConsultingService;
import itba.pod.api.interfaces.VotingService;
import itba.pod.api.interfaces.FiscalizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("Starting server");
        registerServices();
    }

    private static void registerServices() {
        Election election = new Election();

        final AdministrationService administrationService = new AdministrationServiceImpl(election);
        final VotingService votingService = new VotingServiceImpl(election);
        final ConsultingService consultingService = new ConsultingServiceImpl(election);
        final FiscalizationService fiscalizationService = new FiscalizationServiceImpl(election);

        try {
            final Registry registry = LocateRegistry.getRegistry();

            // TODO: Change the ports
            final Remote remoteAdministration = UnicastRemoteObject.exportObject(administrationService, 0);
            final Remote remoteVoting = UnicastRemoteObject.exportObject(votingService, 0);
            final Remote remoteConsulting = UnicastRemoteObject.exportObject(consultingService, 0);
            final Remote remoteFiscalization = UnicastRemoteObject.exportObject(fiscalizationService, 0);

            registry.rebind("administration-service", remoteAdministration);
            logger.info("Administration service has been bound");

            registry.rebind("voting-service", remoteVoting);
            logger.info("Voting service has been bound");

            registry.rebind("consulting-service", remoteConsulting);
            logger.info("Consulting service bound");

            registry.rebind("fiscalization-service", remoteFiscalization);
            logger.info("Fiscalization service has been bound");
        } catch (RemoteException e) {
            logger.error("Remote exception");
        }
    }
}
