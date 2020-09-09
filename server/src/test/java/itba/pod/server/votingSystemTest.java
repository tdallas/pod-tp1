package itba.pod.server;

import itba.pod.api.vote.Candidate;
import itba.pod.api.vote.Ticket;
import itba.pod.api.vote.Vote;
import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.STAR;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class votingSystemTest {
    FPTP f;
    STAR s;
    @Before
    public void setVotes(){

        //STAR
        Ticket t=new Ticket(2,Candidate.TIGER);
        Ticket t1=new Ticket(3,Candidate.LEOPARD);
        Ticket t2=new Ticket(4,Candidate.LYNX);
        Ticket t3=new Ticket(5,Candidate.BUFFALO​);

        List<Ticket> lt=new LinkedList<>();
        lt.add(t);
        lt.add(t1);
        lt.add(t2);
        lt.add(t3);

        //FPTP
        Vote v=new Vote(Candidate.BUFFALO​,lt);
        Vote v1=new Vote(Candidate.BUFFALO​,lt);
        Vote v2=new Vote(Candidate.TIGER,lt);
        Vote v3=new Vote(Candidate.LEOPARD,lt);

        List<Vote> l=new LinkedList<>();
        l.add(v);
        l.add(v1);
        l.add(v2);
        l.add(v3);
        f=new FPTP(l);
        s=new STAR(l);

    }

    @Test
    public void STARTest(){
        Map<Candidate, Integer> m=s.scoringRound();
        System.out.println(m);

    }
    @Test
    public void FPTPTest(){
        Map<Candidate,Double> m=f.result();
        System.out.println(m);

    }






}
