package itba.pod.parser;

import itba.pod.api.model.vote.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VotesParser {
    private final int itemsToParse;

    public VotesParser(int itemsToParse) {
        this.itemsToParse = itemsToParse;
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
        String[] firstThreeSplit = record.get(2).split(",");
        Ticket firstTicket = getTicket(firstThreeSplit[0]);
        Ticket secondTicket = getTicket(firstThreeSplit[1]);
        Ticket thirdTicket = getTicket(firstThreeSplit[2]);
        Ticket fourthTicket = getTicket(record.get(3));
        return Arrays.asList(
                firstTicket,
                secondTicket,
                thirdTicket,
                fourthTicket
        );
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

