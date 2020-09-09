package itba.pod.server.votingSystems;

import itba.pod.api.vote.Candidate;
import itba.pod.api.vote.Ticket;
import itba.pod.api.vote.Vote;

import java.util.*;

import static java.util.stream.Collectors.*;

public class STAR {
    List<Vote> votes;

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public STAR(List<Vote> votes) {
        this.votes = votes;
    }

    public Map<Candidate, Integer> scoringRound(){
        Map<Candidate, Integer> m=new HashMap<>();
        Map<Candidate,Integer> order=new HashMap<>();
        Map<Candidate,Integer> rta=new HashMap<>();


        for(Vote vot:votes){
            m=vot.getTickets().stream().collect(groupingBy(Ticket::getCandidate,summingInt(Ticket::getPoints)));
            order=m.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));
            order.forEach((k, v) -> rta.merge(k, v, Integer::sum));
        }
        return rta;
        }


}
