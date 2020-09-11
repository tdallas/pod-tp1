package itba.pod.parser;

import itba.pod.api.model.vote.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class VotesParserTest {

    private final Candidate tiger = new Candidate("TIGER");
    private final Candidate lynx = new Candidate("LYNX");
    private final Candidate leopard = new Candidate("LEOPARD");
    private final Candidate owl = new Candidate("OWL");
    private final Candidate buffalo = new Candidate("BUFFALO");

    private final Vote expectedFirstVote = new Vote(
            new Table(1000L),
            new State("JUNGLE"),
            Arrays.asList(
                    new Ticket(tiger, 3),
                    new Ticket(leopard, 2),
                    new Ticket(lynx, 1),
                    new Ticket(tiger)
            )
    );

    private final Vote expectedSecondVote = new Vote(
            new Table(1001L),
            new State("JUNGLE"),
            Arrays.asList(
                    new Ticket(lynx, 1),
                    new Ticket(tiger, 1),
                    new Ticket(leopard, 2),
                    new Ticket(lynx)
            )
    );

    private final Vote expectedThirdVote = new Vote(
            new Table(1002L),
            new State("SAVANNAH"),
            Arrays.asList(
                    new Ticket(tiger, 3),
                    new Ticket(lynx, 3),
                    new Ticket(owl, 3),
                    new Ticket(buffalo, 5),
                    new Ticket(buffalo)
            )
    );

    //1000;JUNGLE;TIGER|3,LEOPARD|2,LYNX|1;TIGER
    //1001;JUNGLE;LYNX|1,TIGER|1,LEOPARD|2;LYNX
    //1002;SAVANNAH;TIGER|3,LYNX|3,OWL|3,BUFFALO|5;BUFFALO
    @Test
    public void shouldParser3VotesTest() throws IOException {
        final VotesParser votesParser = new VotesParser();
        final List<Vote> parsedVotes = votesParser.parse(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource("csv_test.csv")
                ).getPath()
        );
        assertEquals("It should have parsed 3 votes", 3, parsedVotes.size());
        final Vote firstVote = parsedVotes.get(0);
        assertEquals(expectedFirstVote, firstVote);
        final Vote secondVote = parsedVotes.get(1);
        assertEquals(expectedSecondVote, secondVote);
        final Vote thirdVote = parsedVotes.get(2);
        assertEquals(expectedThirdVote, thirdVote);

    }


}
