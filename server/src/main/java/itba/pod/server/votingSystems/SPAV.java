package itba.pod.server.votingSystems;

import itba.pod.api.vote.Candidate;
import itba.pod.api.vote.Ticket;
import itba.pod.api.vote.Vote;
import org.omg.CORBA.TCKind;

import java.util.*;
import java.util.stream.Collectors;

public class SPAV {
    private List<Vote> votes;
    private Map<Candidate, Double> winners;
    private int roundsNumber = 3;

    public SPAV(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public Map<Candidate, Double> getWinners() {
        return winners;
    }

    public int getRoundsNumber() {
        return roundsNumber;
    }

    public void calculateScore() {
        winners = new LinkedHashMap<>();
        Map<Candidate, Double> aux = new LinkedHashMap<>();
        boolean contains = false;
        int m = 0;
        for (int i = 0; i < roundsNumber; i++) {
            for (Vote v : votes) {
                //cuento los ganadores en una boleta

                for (Ticket t : v.getTickets()) {
                    //si hay algo en winners fijate si esta en la boleta para auemntar m
                    if (winners.size() != 0) {
                        if (winners.containsKey(t.getCandidate())) {
                            m++;
                            contains = true;
                        }
                    }
                }
                for (Ticket t : v.getTickets()) {
                    //agrego el candidato a aux
                    if (!winners.containsKey(t.getCandidate())) {
                        if (aux.containsKey(t.getCandidate())) {
                            Double app = aux.get(t.getCandidate());
                            aux.put(t.getCandidate(), app + ((double) 1 / (1 + m)));
                        } else {
                            aux.put(t.getCandidate(), ((double) 1 / (1 + m)));
                        }
                    }
                }
                m = 0;
                contains = false;
            }
            Map<Candidate, Double> newMapSortedByValue = aux.entrySet().stream()
                    .sorted(Map.Entry.<Candidate,Double>comparingByValue().reversed().thenComparing(t->t.getKey().toString()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));

            winners.put(newMapSortedByValue.entrySet().iterator().next().getKey(),newMapSortedByValue.entrySet().iterator().next().getValue());
            aux.clear();

        }
    }
}
