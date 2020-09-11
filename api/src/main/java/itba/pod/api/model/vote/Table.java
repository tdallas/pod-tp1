package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

public class Table implements Serializable {
    private long id;
    private final Set<Candidate> fiscalSet;

    public Table(long id) {
        this.id = id;
        this.fiscalSet = new HashSet<>();
    }

    public long getId() {
        return this.id;
    }

    public void registerFiscal(final Candidate fiscal) {
        this.fiscalSet.add(fiscal);
    }

    public boolean hasRegisteredFiscal(final Candidate fiscal) {
        return this.fiscalSet.contains(fiscal);
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
        return Objects.hash(id);
    }
}
