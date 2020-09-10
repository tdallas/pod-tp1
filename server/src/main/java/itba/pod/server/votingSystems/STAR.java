package itba.pod.server.votingSystems;



import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Ticket;
import itba.pod.api.model.vote.Vote;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class STAR implements VotingCalculator{
    private List<Vote> votes;
    private Map<Candidate, Integer> scoring;
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



    public Map<Candidate, Integer> getScoring() {
        return scoring;
    }

    public Double getFirstPercentage() {
        return firstPercentage;
    }

    public Double getSecondPercentage() {
        return secondPercentage;
    }

    public Map<Candidate,Double> calculateScore(){
        scoringRound();
        automaticRunOff();

        Map<Candidate,Double> runoff=new LinkedHashMap<>();
        Object[] keys = this.scoring.keySet().toArray();
        Candidate firstCandidate = this.scoring.entrySet().stream().findFirst().get().getKey();
        Candidate secondCandidate = (Candidate) keys[1];
        if(firstPercentage>secondPercentage){
            runoff.put(firstCandidate,firstPercentage);
            runoff.put(secondCandidate,secondPercentage);
        }
        else if(secondPercentage<firstPercentage){
            runoff.put(secondCandidate,secondPercentage);
            runoff.put(firstCandidate,firstPercentage);
        }
        else{
            if(firstCandidate.toString().compareTo(secondCandidate.toString())<0){
                runoff.put(firstCandidate,firstPercentage);
                runoff.put(secondCandidate,secondPercentage);
            }
            else{
                runoff.put(secondCandidate,secondPercentage);
                runoff.put(firstCandidate,firstPercentage);
            }
        }
        return runoff;
    }

    private void scoringRound() {
        Map<Candidate, Integer> m ;
        Map<Candidate, Integer> order = new HashMap<>();
        Map<Candidate, Integer> rta;

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
    }

    private void automaticRunOff() {
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
        this.firstPercentage =(double)cantFirst/(cantFirst+cantSecond);
        this.secondPercentage =(double)cantSecond/(cantFirst+cantSecond);

    }


}
