package itba.pod.server;

import itba.pod.api.vote.Candidate;
import itba.pod.api.vote.Ticket;
import itba.pod.api.vote.Vote;
import itba.pod.server.votingSystems.FPTP;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class votingSystemTest {
    FPTP f;
    @Before
    public void setVotes(){
        Vote v=new Vote(Candidate.BUFFALO​);
        Vote v1=new Vote(Candidate.BUFFALO​);
        Vote v2=new Vote(Candidate.TIGER);
        Vote v3=new Vote(Candidate.LEOPARD);
        List<Vote> l=new LinkedList<>();
        l.add(v);
        l.add(v1);
        l.add(v2);
        l.add(v3);
        f=new FPTP(l);

    }

    @Test
    public void FPTPTest(){
        Map<Candidate,Double> m=f.result();
        System.out.println(m);

    }






}
