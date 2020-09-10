package itba.pod.client;

import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.*;
import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.SPAV;
import itba.pod.server.votingSystems.STAR;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class consultingClientTest {
    private ConsultingClient cc;
    private FPTP f;
    private STAR s;
    private SPAV sp;
    private Results tableR;
    private Results state;
    private Results national;
    private Results not_init;
    private ConsultingClient cl;
    @Before
    public void setVotes(){

        //STAR
        Ticket t=new Ticket(5, Candidate.JACKALOPE);
        Ticket t1=new Ticket(3,Candidate.LEOPARD);
        Ticket t2=new Ticket(5,Candidate.LYNX);
        Ticket t3=new Ticket(4,Candidate.BUFFALO​);

        Ticket t4=new Ticket(4,Candidate.TIGER);
        Ticket t5=new Ticket(3,Candidate.LEOPARD);
        Ticket t6=new Ticket(2,Candidate.LYNX);
        Ticket t7=new Ticket(1,Candidate.BUFFALO​);
        List<Ticket> lt=new LinkedList<>();
        lt.add(t);
        lt.add(t1);
        lt.add(t2);
        lt.add(t3);

        List<Ticket> lt2=new LinkedList<>();
        lt2.add(t4);
        lt2.add(t5);
        lt2.add(t6);
        lt2.add(t7);

        //FPTP
        Vote v=new Vote(Candidate.BUFFALO​,lt);
        Vote v1=new Vote(Candidate.BUFFALO​,lt2);
        Vote v2=new Vote(Candidate.TIGER,lt);
        Vote v3=new Vote(Candidate.LEOPARD,lt);

        List<Vote> l=new LinkedList<>();
        l.add(v);
        l.add(v1);
        l.add(v2);
        l.add(v3);
        f=new FPTP(l);
        s=new STAR(l);
        sp=new SPAV(l);
        tableR=new Results(Status.FINISHED,f.calculateScore());
        state=new Results(Status.FINISHED,sp.calculateScore());
        national=new Results(Status.FINISHED,s.calculateScore());

    }

    @Test
    public void CSVTestNational() throws IOException, ClientException {
        cl.CSVResults(national,"TestNational",null,null);
    }

    @Test
    public void CSVTestState() throws IOException, ClientException {
        cl.CSVResults(state,"TestState","Jungle",null);
    }

    @Test
    public void CSVTestTable() throws IOException, ClientException {
        cl.CSVResults(tableR,"TestTable",null,"1");

    }
}
