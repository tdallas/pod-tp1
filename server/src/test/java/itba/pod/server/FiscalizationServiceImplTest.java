package itba.pod.server;

import itba.pod.api.interfaces.FiscalizationSubscription;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.*;
import itba.pod.server.elections.Election;
import itba.pod.server.services.FiscalizationServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FiscalizationServiceImplTest {
    private FiscalizationServiceImpl fiscalService;
    private final long tableId  = 1234L;
    private final Table table   = new Table(tableId);
    private final Party party   = new Party("Politicians Party");
    private final Fiscal fiscal = new Fiscal(party, new FiscalizationSubscription() {
        @Override
        public void post(String notification) {

        }

        @Override
        public void consume() {

        }

        @Override
        public void end() {

        }
    });
    private final Candidate newMayor = new Candidate(party.getName());
    private final State capitalCity = new State("Capital City");
    private final List<Ticket> ticketList = new LinkedList<>();
    private Election election   = new Election();

    @Test
    public void testRegister() {
        election.addTable(tableId);

        this.fiscalService = new FiscalizationServiceImpl(election);

        final String expectedMsg = "Fiscal of " + party.getName() + " registered on polling place " + tableId;
        String actualMsg = "";

        try {
            actualMsg = this.fiscalService.register(table.getId(), fiscal);
        } catch (ElectionException ignored) {}

        assert(this.election.getTable(table.getId()).hasRegisteredFiscalFor(party));
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
    public void testNotifyVote() {
        election.addTable(tableId);

        this.fiscalService = new FiscalizationServiceImpl(election);

        try {
            this.fiscalService.register(table.getId(), fiscal);
        } catch (ElectionException ignored) {}

        ticketList.add(new Ticket(newMayor));

        try {
            election.setStatus(Status.INITIALIZED);
            election.emitVote(new Vote(table, capitalCity, ticketList));
        } catch (ElectionException ignored) {}

        // TODO: Check how to test this
    }
}
