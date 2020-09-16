package itba.pod.api.model.vote;

import itba.pod.api.model.election.ElectionException;

import java.io.Serializable;
import java.rmi.RemoteException;
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

    public void registerFiscal(final Fiscal fiscal) {
        this.fiscals.add(fiscal);
    }

    public boolean hasRegisteredFiscalFor(final Party party) {
        return this.fiscals.stream().anyMatch(fiscal -> fiscal.getParty().equals(party));
    }

    public List<Fiscal> getFiscals() {
        return this.fiscals;
    }

    public Fiscal getFiscalOfParty(final Party party) throws ElectionException {
        Optional<Fiscal> optionalFiscal = this.fiscals.stream()
                                                      .filter(fiscal -> fiscal.getParty().equals(party)).findFirst();

        if (optionalFiscal.isPresent())
            return optionalFiscal.get();
        else
            throw new ElectionException("Could not find a fiscal of party " + party.getName() + " in table " + id);
    }

    public void close() {

        this.fiscals.parallelStream().forEach(f -> { try {
            f.endSubscription();
        } catch (RemoteException e) {
            System.out.println(e.toString());
            System.out.println("Remote exception");
        }});
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
