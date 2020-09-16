package itba.pod.client;

import itba.pod.api.interfaces.FiscalizationService;
import itba.pod.api.interfaces.FiscalizationSubscription;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Fiscal;
import itba.pod.api.model.vote.Party;
import itba.pod.api.model.vote.Table;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FiscalizationClient {

    private static final Logger logger = LoggerFactory.getLogger(FiscalizationClient.class);
    private final FiscalizationSubscription subscription = new FiscalizationSubscriptionImpl();
    private String address, partyName;
    private Long tableId;

    public static void main(final String[] args) {
        logger.info("Fiscalization client starting...");

        final FiscalizationClient client = new FiscalizationClient();

        client.parse();
        client.run();
    }

    private void run() {

        try {
            UnicastRemoteObject.exportObject(subscription, 0);
            final FiscalizationService fiscalizationService = (FiscalizationService) Naming
                    .lookup("//" + address + "/fiscalization-service");
            final Table table = new Table(tableId);
            final Fiscal fiscal = new Fiscal(new Party(partyName), this.subscription);
            final String newFiscalRegisteredMsg = fiscalizationService.register(table.getId(), fiscal);

            System.out.println(newFiscalRegisteredMsg);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            logger.info("RMI failure while requesting the fiscalization service: " + e);
        } catch (ElectionException e) {
            logger.info("A problem has occurred while registering a new " + partyName + " fiscal: " + e);
        }
    }

    private void parse() {
        this.address = System.getProperty("serverAddress");
        this.tableId = (long) Integer.parseInt(System.getProperty("id"));
        this.partyName = System.getProperty("party");
    }
}
