package itba.pod.api.vote;

import java.util.List;

public class Vote {
    private final Table table;
    private final Province province;
    // first 3(i = [0,1,2]) are for STAR and SPAV, the last one is for FPTP
    private final List<Ticket> tickets;
    private final Candidate FPTPCandidate;

    public Vote(final Table table, final Province province, final List<Ticket> tickets, Candidate fptpCandidate) {
        this.table = table;
        this.province = province;
        this.tickets = tickets;
        FPTPCandidate = fptpCandidate;
    }

    public Table getTable() {
        return table;
    }

    public Province getProvince() {
        return province;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Candidate getFPTPCandidate() {
        return FPTPCandidate;
    }
}
