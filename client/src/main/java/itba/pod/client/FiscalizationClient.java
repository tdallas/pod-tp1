package itba.pod.client;

import com.beust.jcommander.JCommander;
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
    @Parameter(names = { "-DserverAddress" })
    private String address;
    @Parameter(names = { "-Did" })
    private long tableId;
    @Parameter(names = { "-DpartyName" })
    private String partyName;

    private static final Logger logger = LoggerFactory.getLogger(FiscalizationClient.class);

    public static void main(final String[] args)  {
        logger.info("Fiscalization client starting...");

        final FiscalizationClient client = new FiscalizationClient();

        try {
            JCommander.newBuilder()
                    .addObject(client)
                    .build()
                    .parse(args);
        } catch (NullPointerException e) {
            logger.info("An error occurred while parsing the command line options: " + e);
        }
        client.run();
    }

    private void run() {
        final FiscalizationServiceImpl fiscal;

        try {
            fiscal                = (FiscalizationServiceImpl) Naming.lookup(address);
            final Table table     = new Table(tableId);
            final Candidate party = new Candidate(partyName);

            fiscal.register(table.getId(), party);
            fiscal.addPropertyChangeListener(this);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            logger.info("RMI failure while requesting the fiscalization service: " + e);
        } catch (ElectionException e) {
            logger.info("A problem has occurred while registering a new " + partyName + " fiscal: " + e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String voteNotification = (String) evt.getNewValue();

        System.out.println(voteNotification);
    }
}
