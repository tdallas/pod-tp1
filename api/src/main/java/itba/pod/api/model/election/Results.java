package itba.pod.api.model.election;

import itba.pod.api.model.vote.Candidate;

import java.util.List;
import java.util.Map;

public class Results {
    private Status status;
    private List<Map<Candidate,Double>> winners;

}
