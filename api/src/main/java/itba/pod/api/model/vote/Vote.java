package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vote implements Serializable {
    private final Table table;
    private final State state;
    // first 3(i = [0,1,2]) are for STAR and SPAV, the last one is for FPTP
    private  List<Ticket> tickets;

    public Vote(final Table table, final State state,  List<Ticket> tickets) {
        this.table = table;
        this.state = state;
        this.tickets = tickets;
    }

    public Vote(final List<Ticket> tickets) {
        this.state = null;
        this.table = null;
        this.tickets = tickets;
    }


    public void setTickets(List<Ticket> tickets) {
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
        return "Vote for " + getFPTPCandidate() + " in table " + table + " in " + state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return table != null && state != null && table.equals(vote.table) &&
                state.equals(vote.state) &&
                tickets.equals(vote.tickets);
    }

    public Vote clone(){
        return new Vote(this.table,this.state,this.tickets);
    }

}
