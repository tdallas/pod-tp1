package itba.pod.server.votingSystems;


import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Ticket;
import itba.pod.api.model.vote.Vote;

import java.util.*;
import java.util.stream.Collectors;

public class SPAV implements VotingCalculator{
    //devuelv una lista con en el primer lugar el mapa de la primera vuelta, en el segundo, la segunda, en el tercero la tercera
    private List<Vote> votes;

    //private Map<Candidate, Double> winners;
    private int roundsNumber = 3;

    public SPAV(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Vote> getVotes() {
        return votes;
    }


    public int getRoundsNumber() {
        return roundsNumber;
    }

    // FIXME clean code pls. I think there must be a better way to do this
    public List<Map<Candidate, Double>> calculateScore() {
        Map<Candidate, Double> winners = new LinkedHashMap<>();
        Map<Candidate, Double> aux = new LinkedHashMap<>();
        List<Map<Candidate, Double>> rta = new LinkedList<>();
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

            }
            Map<Candidate, Double> newMapSortedByValue = aux.entrySet().stream()
                    .sorted(Map.Entry.<Candidate,Double>comparingByValue().reversed().thenComparing(t->t.getKey().toString()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
            rta.add(newMapSortedByValue);
            winners.put(newMapSortedByValue.entrySet().iterator().next().getKey(),newMapSortedByValue.entrySet().iterator().next().getValue());
            aux.clear();
        }
        return rta;
    }
}
