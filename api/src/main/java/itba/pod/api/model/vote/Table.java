package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.*;
import java.util.Objects;

public class Table implements Serializable {
    private final long id;
    private final List<Fiscal> fiscals;

    public Table(long id) {
        this.id = id;
        this.fiscals = new LinkedList<>();
    }

    public long getId() {
        return this.id;
    }

    public boolean registerFiscal(final Fiscal fiscal) {
        return this.fiscals.add(fiscal);
    }

    public boolean hasRegisteredFiscalFor(final Party party) {
        return this.fiscals.stream().anyMatch(fiscal -> fiscal.getParty().equals(party));
    }

    public List<Fiscal> getFiscals() {
        return this.fiscals;
    }

    public Fiscal getFiscalOfParty(final Party party) {
        // TODO: Needs 'isPresent()' check
        return this.fiscals.stream().filter(fiscal -> fiscal.getParty().equals(party)).findFirst().get();
    }

    public void close() {
        this.fiscals.parallelStream().forEach(Fiscal::endSubscription);
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
