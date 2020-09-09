package itba.pod.server.votingSystems;

import itba.pod.api.vote.Candidate;
import itba.pod.api.vote.Vote;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FPTP {
    private final List<Vote> votes;


    public FPTP(List<Vote> votes) {
        this.votes = votes;
    }

    public Map<Candidate,Double> result(){
        int size=votes.size();
        return this.votes.stream().collect(Collectors.groupingBy(Vote::getFPTPCandidate, Collectors.collectingAndThen(Collectors.counting(), c -> c/(double)size)));

    }

    public List<Vote> getVotes() {
        return votes;
    }
}
