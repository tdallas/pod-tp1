package itba;

import itba.pod.api.interfaces.FiscalizationSubscription;
import itba.pod.api.model.vote.Fiscal;
import itba.pod.api.model.vote.Party;
import itba.pod.api.model.vote.Table;
import org.junit.jupiter.api.Test;

public class TableTest {
    private final long id = 12345;
    private final Table table = new Table(this.id);
    private final Party party = new Party("Politics Party");
    private final Fiscal fiscal = new Fiscal(party, new FiscalizationSubscription() {
        @Override
        public void post(String notification) {

        }

        @Override
        public void consume() {

        }

        @Override
        public void end() {

        }
    });

    @Test
    public void testRegisterFiscal() {
        table.registerFiscal(fiscal);
        assert(table.getFiscals().contains(fiscal));
    }

    @Test
    public void testHasRegisteredFiscal() {
        table.registerFiscal(fiscal);
        assert(table.hasRegisteredFiscalFor(party));
    }
}
