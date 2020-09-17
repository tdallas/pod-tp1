package itba.pod.client;

import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsultingClientTest {
    private Results tableR;
    private Results state;
    private Results national;


    List<Vote> l = new LinkedList<>();

    private final Candidate tiger = new Candidate("TIGER");
    private final Candidate lynx = new Candidate("LYNX");
    private final Candidate leopard = new Candidate("LEOPARD");
    private final Candidate owl = new Candidate("OWL");
    private final Candidate buffalo = new Candidate("BUFFALO");
    private final Candidate jackalope = new Candidate("JACKALOPE");
    private Map<Candidate, Double> fptp = new LinkedHashMap<>();
    private Map<Candidate, Double> star = new LinkedHashMap<>();
    private Map<Candidate, Double> star2 = new LinkedHashMap<>();
    private Map<Candidate, Double> spav = new LinkedHashMap<>();
    private Map<Candidate, Double> spav2 = new LinkedHashMap<>();
    private Map<Candidate, Double> spav3 = new LinkedHashMap<>();

    private List<Map<Candidate, Double>> fptpL = new LinkedList<>();
    private List<Map<Candidate, Double>> starL = new LinkedList<>();
    private List<Map<Candidate, Double>> spavL = new LinkedList<>();


    @BeforeEach
    public void setVotes() {
        fptp.put(buffalo, 0.75);
        fptp.put(owl, 0.25);
        fptpL.add(fptp);
        spav.put(leopard, 4.0);
        spav.put(lynx, 4.0);
        spav.put(jackalope, 3.0);
        spav.put(tiger, 1.0);
        spavL.add(spav);
        spav2.put(lynx, 2.0);
        spav2.put(jackalope, 1.5);
        spav2.put(tiger, 0.5);
        spavL.add(spav2);
        spav3.put(jackalope, 1.0);
        spav3.put(tiger, (double) 1 / 3);
        spavL.add(spav3);
        star.put(lynx, 17.0);
        star.put(jackalope, 15.0);
        star.put(leopard, 12.0);
        star.put(tiger, 4.0);
        star.put(owl, 0.0);
        star.put(buffalo, 0.0);
        starL.add(star);
        star2.put(jackalope, 0.75);
        star2.put(lynx, 0.25);
        starL.add(star2);


        tableR = new Results(Status.FINISHED, fptpL);
        state = new Results(Status.FINISHED, spavL);
        national = new Results(Status.FINISHED, starL);

    }

    @Test
    public void CSVTestNational() throws ClientException {
        ConsultingClient.CSVResults(national, "TestNational", null, null);
        //scoring winner
        assertEquals(lynx, starL.get(0).keySet().toArray()[0]);
        //runoff winner
        assertEquals(jackalope, starL.get(0).keySet().toArray()[1]);

    }

    @Test
    public void CSVTestState() throws ClientException {
        ConsultingClient.CSVResults(state, "TestState", "JUNGLE", null);
        assertEquals(leopard, spavL.get(0).keySet().toArray()[0]);
        assertEquals(lynx, spavL.get(0).keySet().toArray()[1]);
        assertEquals(jackalope, spavL.get(0).keySet().toArray()[2]);
        //LEOPARD,LYNX,BUFFALO
    }

    @Test
    public void CSVTestTable() throws ClientException {
        ConsultingClient.CSVResults(tableR, "TestTable", null, "1");
        assertEquals(buffalo, fptpL.get(0).keySet().stream().findFirst().get());

    }
}