package itba;

import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Table;
import org.junit.jupiter.api.Test;

public class TableTest {
    private final long id = 12345;
    private final Table table = new Table(this.id);

    @Test
    public void testRegisterFiscal() {
        final Candidate fiscal = new Candidate("Pepe");

        table.registerFiscal(fiscal);
        assert(table.getFiscalSet().contains(fiscal));
    }

    @Test
    public void testHasRegisteredFiscal() {
        final Candidate fiscal = new Candidate("Pepe");

        table.registerFiscal(fiscal);
        assert(table.hasRegisteredFiscal(fiscal));
    }
}
