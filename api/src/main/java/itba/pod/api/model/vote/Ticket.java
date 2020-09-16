package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.Objects;

public class Ticket implements Serializable {
    private final Candidate candidate;
    private int points;

    public Ticket(final Candidate candidate) {
        this.candidate = candidate;
    }

    public Ticket(final Candidate candidate, final int points) {
        this.points = points;
        this.candidate = candidate;
    }

    public int getPoints() {
        return points;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return points == ticket.points &&
                candidate.equals(ticket.candidate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidate, points);
    }
}
