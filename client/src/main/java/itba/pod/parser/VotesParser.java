package itba.pod.parser;

import itba.pod.api.model.vote.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VotesParser {

    public VotesParser() {
    }

    /**
     * We assume *province* in each line is from a list of predefined values
     *
     * @param pathToCsv : String to csv file path
     * @return
     */
    public List<Vote> parse(final String pathToCsv) throws IOException {
        Reader in = new FileReader(pathToCsv);
        Iterable<CSVRecord> records = CSVFormat.newFormat(';').parse(in);
        List<Vote> votes = new ArrayList<>();

        records.forEach(record -> {
            Table table = new Table(Long.parseLong(record.get(0)));
            State state = new State(record.get(1));
            List<Ticket> tickets = getTickets(record);
            votes.add(
                    new Vote(
                            table,
                            state,
                            tickets
                    )
            );
        });
        return votes;
    }

    private List<Ticket> getTickets(final CSVRecord record) {
        String[] ticketsWithPoints = record.get(2).split(",");

        final List<Ticket> tickets = new ArrayList<>(ticketsWithPoints.length + 1);

        for (String ticketsWithPoint : ticketsWithPoints) {
            tickets.add(getTicket(ticketsWithPoint));
        }

        final Ticket lastTicket = getTicket(record.get(3));
        tickets.add(lastTicket);

        return tickets;
    }

    private Ticket getTicket(final String ticketString) {
        String[] ticketsSplit = ticketString.split("\\|");
        return ticketsSplit.length > 1 ?
                new Ticket(
                        new Candidate(ticketsSplit[0]),
                        Integer.parseInt(ticketsSplit[1])
                ) :
                new Ticket(
                        new Candidate(ticketsSplit[0])
                );
    }
}

