package itba.pod.client;

import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.*;
import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.SPAV;
import itba.pod.server.votingSystems.STAR;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ConsultingClientTest {
    private Results tableR;
    private Results state;
    private Results national;

    @Before
    public void setVotes(){

        //STAR
        Ticket t = new Ticket(new Candidate("JACKALOPE"), 5);
        Ticket t1 = new Ticket(new Candidate("LEOPARD"), 3);
        Ticket t2 = new Ticket(new Candidate("LYNX"), 5);
        Ticket t3 = new Ticket(new Candidate("BUFFALO​"), 4);

        Ticket t4 = new Ticket(new Candidate("TIGER"), 4);
        Ticket t5 = new Ticket(new Candidate("LEOPARD"), 3);
        Ticket t6 = new Ticket(new Candidate("LYNX"), 2);
        Ticket t7 = new Ticket(new Candidate("OWL"), 1);
        List<Ticket> lt = new LinkedList<>();
        lt.add(t);
        lt.add(t1);
        lt.add(t2);
        lt.add(t3);

        List<Ticket> lt2 = new LinkedList<>();
        lt2.add(t4);
        lt2.add(t5);
        lt2.add(t6);
        lt2.add(t7);

        //FPTP
        Vote v = new Vote(lt);
        Vote v1 = new Vote(lt2);
        Vote v2 = new Vote(lt);
        Vote v3 = new Vote(lt);
        List<Vote> l=new LinkedList<>();
        l.add(v);
        l.add(v1);
        l.add(v2);
        l.add(v3);
        FPTP f = new FPTP(l);
        STAR s = new STAR(l);
        SPAV sp = new SPAV(l);
        tableR=new Results(Status.FINISHED, f.calculateScore());
        state=new Results(Status.FINISHED, sp.calculateScore());
        national=new Results(Status.FINISHED, s.calculateScore());

    }

    @Test
    public void CSVTestNational() throws ClientException {
        ConsultingClient.CSVResults(national,"TestNational", null, null);
    }

    @Test
    public void CSVTestState() throws ClientException {
        ConsultingClient.CSVResults(state,"TestState", "JUNGLE", null);
    }

    @Test
    public void CSVTestTable() throws ClientException {
        ConsultingClient.CSVResults(tableR,"TestTable", null, "1");

    }
}
