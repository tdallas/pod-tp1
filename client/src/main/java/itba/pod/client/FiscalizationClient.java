package itba.pod.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import itba.pod.api.interfaces.FiscalizationService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Table;
import itba.pod.server.services.FiscalizationServiceImpl;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.beust.jcommander.Parameter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class FiscalizationClient implements PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(FiscalizationClient.class);

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
            } catch (NullPointerException e) {
                logger.info("An error occurred while parsing the command line options: " + e);
            }
        }
    }

    public static void main(final String[] args) {
        logger.info("Fiscalization client starting...");

        final FiscalizationClient client = new FiscalizationClient();

        client.run(args);
    }

    private void run(final String[] args) {
        final ArgsParser parser = new ArgsParser();

        try {
            parser.parse(args);
        } catch (ParameterException | NullPointerException e) {
            logger.info("Not enough arguments, make sure to pass the server address, the polling place ID and the " +
                    "fiscal");
        }

        logger.info("//" + parser.address + "/fiscalization-service");
        try {
            final FiscalizationService fiscal = (FiscalizationService) Naming
                    .lookup("//" + parser.address + "/fiscalization-service");
            final Table table = new Table(parser.tableId);
            final Candidate party = new Candidate(parser.partyName);
            final String newFiscalRegisteredMsg = fiscal.register(table.getId(), party);

            System.out.println(newFiscalRegisteredMsg);

            fiscal.addPropertyChangeListener(this);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            logger.info("RMI failure while requesting the fiscalization service: " + e);
        } catch (ElectionException e) {
            logger.info("A problem has occurred while registering a new " + parser.partyName + " fiscal: " + e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String voteNotification = (String) evt.getNewValue();

        System.out.println(voteNotification);
    }
}
