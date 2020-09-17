package itba.pod.server.votingSystems;


import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Ticket;
import itba.pod.api.model.vote.Vote;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class STAR implements VotingCalculator {
    //devuelve una lista con un mapa. el primer elemento de la lista es el mapa de scoring, el segundo elemento es el mapa del runoff
    private List<Vote> votes;
    private Map<Candidate, Double> scoring;
    private Double firstPercentage;
    private Double secondPercentage;

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public STAR(List<Vote> votes) {
        this.votes = votes;
    }


    public Map<Candidate, Double> getScoring() {
        return scoring;
    }

    public Double getFirstPercentage() {
        return firstPercentage;
    }

    public Double getSecondPercentage() {
        return secondPercentage;
    }

    public List<Map<Candidate, Double>> calculateScore() {
        scoringRound();
        automaticRunOff();

        List<Map<Candidate, Double>> runoff = new LinkedList<>();
        Map<Candidate, Double> aux = new LinkedHashMap<>();
        runoff.add(scoring);

        Object[] keys = this.scoring.keySet().toArray();
        Candidate firstCandidate = this.scoring.entrySet().stream().findFirst().get().getKey();
        Candidate secondCandidate = (Candidate) keys[1];
        if (firstPercentage > secondPercentage) {
            aux.put(firstCandidate, firstPercentage);
            aux.put(secondCandidate, secondPercentage);
        } else if (secondPercentage > firstPercentage) {
            aux.put(secondCandidate, secondPercentage);
            aux.put(firstCandidate, firstPercentage);
        } else {
            if (firstCandidate.toString().compareTo(secondCandidate.toString()) < 0) {
                aux.put(firstCandidate, firstPercentage);
                aux.put(secondCandidate, secondPercentage);
            } else {
                aux.put(secondCandidate, secondPercentage);
                aux.put(firstCandidate, firstPercentage);
            }
        }
        runoff.add(aux);
        return runoff;
    }

    private void scoringRound() {
        Map<Candidate, Double> m;
        Map<Candidate, Double> order = new HashMap<>();
        Map<Candidate, Double> rta;

        for (Vote vot : votes) {
            m = vot.getTickets().stream().collect(groupingBy(Ticket::getCandidate, summingDouble(Ticket::getPoints)));
            m.forEach((k, v) -> order.merge(k, v, Double::sum));
        }
        rta = order.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        this.scoring = rta;
    }

    private void automaticRunOff() {
        Object[] keys = this.scoring.keySet().toArray();
        Candidate first = this.scoring.entrySet().stream().findFirst().get().getKey();
        Candidate second = (Candidate) keys[1];
        int cantFirst = 0;
        int cantSecond = 0;
        Map<Candidate, Integer> rta = new HashMap<>();
        for (Vote vot : votes) {
            List<Ticket> l = vot.getTickets().stream().filter(t -> t.getCandidate().equals(first) || t.getCandidate().equals(second)).sorted(Comparator.comparing(Ticket::getPoints).reversed().thenComparing(t -> t.getCandidate().toString())).collect(Collectors.toList());

            if (l.size() != 0) {
                if (l.get(0).getCandidate().equals(first)) {
                    cantFirst++;
                } else if (l.get(0).getCandidate().equals(second)) {
                    cantSecond++;
                }
            }
        }
        this.firstPercentage = (double) cantFirst / (cantFirst + cantSecond);
        this.secondPercentage = (double) cantSecond / (cantFirst + cantSecond);

    }


}
