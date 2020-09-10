package itba.pod.api.model.vote;

import java.util.List;

public class Vote {
    private final Table table;
    private final State state;
    // first 3(i = [0,1,2]) are for STAR and SPAV, the last one is for FPTP
    private final List<Ticket> tickets;

    public Vote(final Table table, final State state, final List<Ticket> tickets) {
        this.table = table;
        this.state = state;
        this.tickets = tickets;
    }

    public Vote(final List<Ticket> tickets) {
        this.state = null;
        this.table = null;
        this.tickets = tickets;
    }

    public Table getTable() {
        return table;
    }

    public State getState() {
        return state;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Candidate getFPTPCandidate() {
        return tickets.get(tickets.size() - 1).getCandidate();
    }

    @Override
    public String toString() {
        return "Vote for " + getFPTPCandidate() + " in table " + table.toString() + " in " + state.toString();
    }
}
