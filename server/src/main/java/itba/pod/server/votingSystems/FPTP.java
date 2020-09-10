package itba.pod.server.votingSystems;



import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Vote;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FPTP implements VotingCalculator {
    private final List<Vote> votes;
    //private  Map<Candidate,Double> resutls;

    public FPTP(List<Vote> votes) {
        this.votes = votes;
    }

    public Map<Candidate,Double> calculateScore(){
        int size=votes.size();
        Map<Candidate,Double> winners=new LinkedHashMap<>();
        winners= this.votes.stream().collect(Collectors.groupingBy(Vote::getFPTPCandidate, Collectors.collectingAndThen(Collectors.counting(), c -> c/(double)size)));
        return winners.entrySet().stream()
                .sorted(Map.Entry.<Candidate,Double>comparingByValue().reversed().thenComparing(t->t.getKey().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
    }



    public List<Vote> getVotes() {
        return votes;
    }


}
