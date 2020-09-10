package itba.pod.api.model.election;

import itba.pod.api.model.vote.Candidate;

import java.util.List;
import java.util.Map;

public class Results {
    private Status status;
    private List<Map<Candidate,Double>> winners;

    public Results(Status status, List<Map<Candidate, Double>> winners) {
        this.status = status;
        this.winners = winners;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Map<Candidate, Double>> getWinners() {
        return winners;
    }

    public void setWinners(List<Map<Candidate, Double>> winners) {
        this.winners = winners;
    }
}
