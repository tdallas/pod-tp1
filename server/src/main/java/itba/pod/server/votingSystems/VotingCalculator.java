package itba.pod.server.votingSystems;



import itba.pod.api.model.vote.Candidate;

import java.util.List;
import java.util.Map;

public interface VotingCalculator {
   List<Map<Candidate,Double>> calculateScore();
}
