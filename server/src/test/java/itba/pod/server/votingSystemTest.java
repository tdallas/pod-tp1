package itba.pod.server;

import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Ticket;
import itba.pod.api.model.vote.Vote;

import itba.pod.server.votingSystems.FPTP;
import itba.pod.server.votingSystems.SPAV;
import itba.pod.server.votingSystems.STAR;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class votingSystemTest {
    FPTP f;
    STAR s;
    SPAV sp;
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

    }

    @Test
    public void STARTest(){
        Map<Candidate,Double> m=s.calculateScore();
        System.out.println("START Score Counting"+"\n"+s.getScoring());
        System.out.println("First: "+s.getFirstPercentage()+" Second: "+s.getSecondPercentage());
        System.out.println("START Runoff"+"\n"+m);
    }

    @Test
    public void FPTPTest(){
        System.out.println("FPTP"+"\n"+f.calculateScore());

    }

    @Test
    public void SPAVTest(){
        System.out.println("SPAV"+"\n"+sp.calculateScore());

    }






}
