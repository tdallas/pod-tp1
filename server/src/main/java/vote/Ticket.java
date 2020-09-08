package vote;

public class Ticket {
    private final String candidate;
    private int points;

    public Ticket(final String candidate) {
        this.candidate = candidate;
    }

    public Ticket(final int points, final String candidate) {
        this.points = points;
        this.candidate = candidate;
    }

    public int getPoints() {
        return points;
    }

    public String getCandidate() {
        return candidate;
    }
}
