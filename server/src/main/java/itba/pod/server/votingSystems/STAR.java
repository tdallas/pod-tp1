package itba.pod.server.votingSystems;

import itba.pod.api.vote.Candidate;
import itba.pod.api.vote.Ticket;
import itba.pod.api.vote.Vote;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class STAR {
    List<Vote> votes;
    Map<Candidate, Integer> scoring;
    Double first;
    Double second;

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public STAR(List<Vote> votes) {
        this.votes = votes;
    }

    public Map<Candidate, Integer> scoringRound() {
        Map<Candidate, Integer> m = new HashMap<>();
        Map<Candidate, Integer> order = new HashMap<>();
        Map<Candidate, Integer> rta = new HashMap<>();


        for (Vote vot : votes) {
            m = vot.getTickets().stream().collect(groupingBy(Ticket::getCandidate, summingInt(Ticket::getPoints)));
            m.forEach((k, v) -> order.merge(k, v, Integer::sum));
        }
        rta = order.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        this.scoring = rta;
        return rta;
    }

    public void automaticRunOff() {
        Object[] keys = this.scoring.keySet().toArray();
        Candidate first = this.scoring.entrySet().stream().findFirst().get().getKey();
        Candidate second = (Candidate) keys[1];
        int cantFirst = 0;
        int cantSecond = 0;
        Map<Candidate, Integer> rta = new HashMap<>();
        for (Vote vot : votes) {
            List<Ticket> l = vot.getTickets().stream().filter(t -> t.getCandidate() == first || t.getCandidate() == second).sorted(Comparator.comparing(Ticket::getPoints).reversed().thenComparing(t->t.getCandidate().toString())).collect(Collectors.toList());

            if (l.size() != 0) {
                if (l.get(0).getCandidate() == first) {
                    cantFirst++;
                } else if (l.get(0).getCandidate() == second) {
                    cantSecond++;
                }
            }
        }
        this.first=(double)cantFirst/(cantFirst+cantSecond);
        this.second=(double)cantSecond/(cantFirst+cantSecond);
        System.out.println("1:"+this.first+" 2:"+this.second);
        

    }


}
