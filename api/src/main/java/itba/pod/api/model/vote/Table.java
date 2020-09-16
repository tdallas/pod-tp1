package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Table implements Serializable {
    private final long id;
    private final Set<Fiscal> fiscalSet;

    public Table(long id) {
        this.id = id;
        this.fiscalSet = new HashSet<>();
    }

    public long getId() {
        return this.id;
    }

    public boolean registerFiscal(final Fiscal fiscal) {
        return this.fiscalSet.add(fiscal);
    }

    public boolean hasRegisteredFiscalFor(final Party party) {
        return this.fiscalSet.stream().anyMatch(fiscal -> fiscal.getParty().equals(party));
    }

    public Set<Fiscal> getFiscalSet() {
        return this.fiscalSet;
    }

    public Fiscal getFiscalOfParty(final Party party) {
        // TODO: Needs 'isPresent()' check
        return this.fiscalSet.stream().filter(fiscal -> fiscal.getParty().equals(party)).findFirst().get();
    }

    public void close() {
        this.fiscalSet.parallelStream().forEach(Fiscal::endSubscription);
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id == table.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
