package itba.pod.server.votingSystems;



import itba.pod.api.model.vote.Candidate;

import java.util.Map;

public interface VotingCalculator {
    Map<Candidate,Double> calculateScore();
}
