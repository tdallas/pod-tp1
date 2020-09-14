package itba.pod.server;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Table;
import itba.pod.server.elections.Election;
import itba.pod.server.services.FiscalizationServiceImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.beans.PropertyChangeListener;

public class FiscalizationServiceImplTest {
    private FiscalizationServiceImpl fiscalService;
    private final long tableId      = 1234L;
    private final Table table       = new Table(tableId);
    private final Candidate fiscal  = new Candidate("Pepe");
    private Election election       = new Election();

    @Test
    public void testRegister() {
        election.addTable(tableId);

        this.fiscalService = new FiscalizationServiceImpl(election);

        final String expectedMsg = "Fiscal of " + fiscal.getName() + " registered on polling place " + tableId;
        String actualMsg = "";

        try {
            actualMsg = this.fiscalService.register(table.getId(), fiscal);
        } catch (ElectionException ignored) {}

        assert(this.election.getTable(table.getId()).hasRegisteredFiscal(fiscal));
        assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void testRegisterExceptionWhenElectionInitialized() {
        election = new Election(Status.FINISHED);

        election.addTable(tableId);

        this.fiscalService  = new FiscalizationServiceImpl(election);
        Exception exception = assertThrows(ElectionException.class, () ->
            this.fiscalService.register(table.getId(), fiscal)
        );

        System.out.println("Exception thrown: " + exception.getMessage());
    }

    @Test
    public void testRegisterExceptionWhenElectionFinished() {
        election = new Election(Status.FINISHED);

        election.addTable(tableId);

        this.fiscalService  = new FiscalizationServiceImpl(election);
        Exception exception = assertThrows(ElectionException.class, () ->
            this.fiscalService.register(table.getId(), fiscal)
        );

        System.out.println("Exception thrown: " + exception.getMessage());
    }

    @Test
    // TODO: Refactor this, this isn't an actual test, the event message needs to be obtained and compared
    public void testNotifyVote() {
        election.addTable(tableId);

        this.fiscalService = new FiscalizationServiceImpl(election);

        try {
            this.fiscalService.register(table.getId(), fiscal);
        } catch (ElectionException ignored) {}

        PropertyChangeListener observer = evt ->  {
            final String voteNotification = (String) evt.getNewValue();

            System.out.println(voteNotification);
        };

        this.fiscalService.addPropertyChangeListener(observer);
        this.fiscalService.notifyVote(table.getId(), fiscal);
    }
}
