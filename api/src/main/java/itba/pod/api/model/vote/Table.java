package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.Objects;

public class Table implements Serializable {
    private long id;

    public Table() {
    }

    public Table(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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
