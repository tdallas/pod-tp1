package itba.pod.server;

import itba.pod.server.elections.Election;
import itba.pod.server.services.AdministrationService;
import itba.pod.server.services.FiscalizationServiceImpl;
import itba.pod.server.services.VotingService;
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

        final AdministrationService administrationService = new AdministrationService(election);
        final VotingService votingService = new VotingService(election);
        final FiscalizationServiceImpl fiscalizationService = new FiscalizationServiceImpl(election);

        try {
            final Registry registry = LocateRegistry.getRegistry();

            // TODO: Change the ports
            final Remote remoteAdministration = UnicastRemoteObject.exportObject(administrationService, 0);
            final Remote remoteVoting = UnicastRemoteObject.exportObject(votingService, 0);
            final Remote remoteFiscalization = UnicastRemoteObject.exportObject(fiscalizationService, 0);

            registry.rebind("administration-service", remoteAdministration);
            logger.info("Administration service has been bound");

            registry.rebind("voting-service", remoteVoting);
            logger.info("Voting service has been bound");

            registry.rebind("fiscalization-service", remoteFiscalization);
            logger.info("Fiscalization service has been bound");
        } catch (RemoteException e) {
            logger.error("Remote exception");
        }
    }
}
