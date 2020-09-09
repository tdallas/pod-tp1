package itba.pod.server.votingSystems;

import itba.pod.api.vote.Candidate;
import itba.pod.api.vote.Vote;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FPTP {
    private final List<Vote> votes;
    private  Map<Candidate,Double> resutls;

    public FPTP(List<Vote> votes) {
        this.votes = votes;
    }

    public void calculateScore(){
        int size=votes.size();
        this.resutls=new LinkedHashMap<>();
        this.resutls= this.votes.stream().collect(Collectors.groupingBy(Vote::getFPTPCandidate, Collectors.collectingAndThen(Collectors.counting(), c -> c/(double)size)));

    }

    public List<Vote> getVotes() {
        return votes;
    }

    public Map<Candidate, Double> getResutls() {
        return resutls;
    }
}
