package itba.pod.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import itba.pod.api.interfaces.FiscalizationService;
import itba.pod.api.interfaces.FiscalizationSubscription;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Fiscal;
import itba.pod.api.model.vote.FiscalizationSubscriptionImpl;
import itba.pod.api.model.vote.Party;
import itba.pod.api.model.vote.Table;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.beust.jcommander.Parameter;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class FiscalizationClient {

    private static final Logger logger = LoggerFactory.getLogger(FiscalizationClient.class);
    private final FiscalizationSubscription subscription = new FiscalizationSubscriptionImpl();

    public static void main(final String[] args) {
        logger.info("Fiscalization client starting...");

        final FiscalizationClient client = new FiscalizationClient();

        client.run(args);
    }

    private void run(final String[] args) {
        final ArgsParser parser = new ArgsParser();

        parser.parse(args);

        try {
            final FiscalizationService fiscalizationService = (FiscalizationService) Naming
                    .lookup("//" + parser.address + "/fiscalization-service");
            final Table table = new Table(parser.tableId);
            final Fiscal fiscal = new Fiscal(new Party(parser.partyName), this.subscription);
            final String newFiscalRegisteredMsg = fiscalizationService.register(table.getId(), fiscal);

            System.out.println(newFiscalRegisteredMsg);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            logger.info("RMI failure while requesting the fiscalization service: " + e);
        } catch (ElectionException e) {
            logger.info("A problem has occurred while registering a new " + parser.partyName + " fiscal: " + e);
        }

        for (;;);
    }

    @Parameters(separators = "=")
    private class ArgsParser {
        @Parameter(names = {"-DserverAddress"})
        private String address;
        @Parameter(names = {"-Did"})
        private long tableId;
        @Parameter(names = {"-Dparty"})
        private String partyName;

        private void parse(final String[] args) {
            try {
                JCommander.newBuilder()
                        .addObject(this)
                        .build()
                        .parse(args);
            } catch (NullPointerException | ParameterException e) {
                logger.info("An error occurred while parsing the command line options: " + e);
            }
        }
    }
}
