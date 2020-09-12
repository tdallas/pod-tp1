package itba.pod.client;

import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.*;
import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.SPAV;
import itba.pod.server.votingSystems.STAR;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConsultingClientTest {
    private Results tableR;
    private Results state;
    private Results national;

    private FPTP f; //mesas
    private STAR s; //nacionales
    private SPAV sp; //provincial
    List<Vote> l = new LinkedList<>();

    private final Candidate tiger = new Candidate("TIGER");
    private final Candidate lynx = new Candidate("LYNX");
    private final Candidate leopard = new Candidate("LEOPARD");
    private final Candidate owl = new Candidate("OWL");
    private final Candidate buffalo = new Candidate("BUFFALO");
    private final Candidate jackalope = new Candidate("JACKALOPE");


    private final Vote firstVote = new Vote(
            new Table(1001L),
            new State("JUNGLE"),
            Arrays.asList(
                    new Ticket(jackalope, 5),
                    new Ticket(leopard, 3),
                    new Ticket(lynx, 5),
                    new Ticket(buffalo)
            )
    );

    private final Vote secondVote = new Vote(
            new Table(1002L),
            new State("JUNGLE"),
            Arrays.asList(
                    new Ticket(tiger, 4),
                    new Ticket(leopard, 3),
                    new Ticket(lynx, 2),
                    new Ticket(owl)
            )
    );

    @Before
    public void setVotes() {

        List<Vote> l = new LinkedList<>();
        l.add(firstVote);
        l.add(secondVote);
        l.add(firstVote);
        l.add(firstVote);

        f = new FPTP(l);
        s = new STAR(l);
        sp = new SPAV(l);
        tableR = new Results(Status.FINISHED, f.calculateScore());
        state = new Results(Status.FINISHED, sp.calculateScore());
        national = new Results(Status.FINISHED, s.calculateScore());

    }

    @Test
    public void CSVTestNational() throws ClientException {
        ConsultingClient.CSVResults(national, "TestNational", null, null);
        List<Map<Candidate,Double>> expected=s.calculateScore();
        //scoring winner
        assertEquals(lynx,expected.get(0).keySet().toArray()[0]);
        //runoff winner
        assertEquals(jackalope, expected.get(0).keySet().toArray()[1]);

    }

    @Test
    public void CSVTestState() throws ClientException {
        ConsultingClient.CSVResults(state, "TestState", "JUNGLE", null);
        List<Map<Candidate,Double>> expected=sp.calculateScore();
        assertEquals(leopard, expected.get(0).keySet().toArray()[0]);
        assertEquals(lynx, expected.get(0).keySet().toArray()[1]);
        assertEquals(buffalo, expected.get(0).keySet().toArray()[2]);
        //LEOPARD,LYNX,BUFFALO
    }

    @Test
    public void CSVTestTable() throws ClientException {
        ConsultingClient.CSVResults(tableR, "TestTable", null, "1");
        assertEquals(buffalo, f.calculateScore().get(0).keySet().stream().findFirst().get());

    }
}