package itba.pod.server;

import itba.pod.server.elections.Election;
import itba.pod.server.services.AdministrationServiceImpl;
import itba.pod.server.services.VotingServiceImpl;
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
        logger.info("Server Starting ...");
        registerServices();
    }

    private static void registerServices() {
        Election election = new Election();

        final AdministrationServiceImpl administrationService = new AdministrationServiceImpl(election);
        final VotingServiceImpl votingService = new VotingServiceImpl(election);

        try {
            final Registry registry = LocateRegistry.getRegistry();

            final Remote remoteAdministration = UnicastRemoteObject.exportObject(administrationService, 0);
            final Remote remoteVoting = UnicastRemoteObject.exportObject(votingService, 0);

            registry.rebind("administration-service", remoteAdministration);
            logger.info("Administration service bound");

            registry.rebind("voting-service", remoteVoting);
            logger.info("Voting service bound");

        } catch (RemoteException e) {
            logger.error("Remote exception");
        }
    }
}
