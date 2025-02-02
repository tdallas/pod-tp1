package itba.pod.server;

import itba.pod.api.model.vote.*;

import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.SPAV;
import itba.pod.server.votingSystems.STAR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class VotingSystemTest {
    private FPTP f; //mesas
    private STAR s; //nacionales
    private SPAV sp; //provincial

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

    @BeforeEach
    public void setVotes() {

        List<Vote> l = new LinkedList<>();
        l.add(firstVote);
        l.add(secondVote);
        l.add(firstVote);
        l.add(firstVote);

        f = new FPTP(l);
        s = new STAR(l);
        sp = new SPAV(l);


    }

    @Test
    public void STARTest() {
        List<Map<Candidate, Double>> m = s.calculateScore();
        Map<Candidate,Double> scoringExpected = new LinkedHashMap<>();
        Map<Candidate,Double> runoffExpected = new LinkedHashMap<>();
        scoringExpected.put(lynx,17.0);
        scoringExpected.put(jackalope,15.0);
        scoringExpected.put(leopard,12.0);
        scoringExpected.put(tiger,4.0);
        scoringExpected.put(owl,0.0);
        scoringExpected.put(buffalo,0.0);
        runoffExpected.put(jackalope,0.75);
        runoffExpected.put(lynx,0.25);

        assertEquals(scoringExpected, m.get(0));
        assertEquals(runoffExpected, m.get(1));
        System.out.println("START\n"+m);
    }

    @Test
    public void FPTPTest() {
        Map<Candidate,Double> fptpExpected=new LinkedHashMap<>();
        fptpExpected.put(buffalo,0.75);
        fptpExpected.put(owl,0.25);
        assertEquals(fptpExpected,f.calculateScore().get(0));
        System.out.println("FPTP\n"+f.calculateScore());
    }

    @Test
    public void SPAVTest() {
        List<Map<Candidate,Double>> expected;
        Map<Candidate,Double> firstRunExpected = new LinkedHashMap<>();
        Map<Candidate,Double> secondRunExpected = new LinkedHashMap<>();
        Map<Candidate,Double> thirdRunExpected = new LinkedHashMap<>();
        firstRunExpected.put(leopard,4.0);
        firstRunExpected.put(lynx,4.0);
        firstRunExpected.put(jackalope,3.0);
        firstRunExpected.put(tiger,1.0);
        secondRunExpected.put(lynx,2.0);
        secondRunExpected.put(jackalope,1.5);
        secondRunExpected.put(tiger,0.5);
        thirdRunExpected.put(jackalope,1.0);
        thirdRunExpected.put(tiger,(double)1/3);

        expected=sp.calculateScore();
        assertEquals(firstRunExpected,expected.get(0));
        assertEquals(secondRunExpected,expected.get(1));
        assertEquals(thirdRunExpected,expected.get(2));
        System.out.println("SPAV\n" + expected);
    }

}