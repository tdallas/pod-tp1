package itba.pod.api.vote;

public class Ticket {
    private final Candidate candidate;
    private int points;

    public Ticket(final Candidate candidate) {
        this.candidate = candidate;
    }

    public Ticket(final int points, final Candidate candidate) {
        this.points = points;
        this.candidate = candidate;
    }

    public int getPoints() {
        return points;
    }

    public Candidate getCandidate() {
        return candidate;
    }
}
