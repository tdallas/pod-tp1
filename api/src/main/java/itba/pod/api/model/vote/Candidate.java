package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.Objects;

public class Candidate implements Serializable {
    private final Party party;

    public Candidate(final String name) {
        this.party = new Party(name);
    }

    public Party getParty() {
        return this.party;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Candidate otherCandidate = (Candidate) other;

        return Objects.equals(this.party.getName(), otherCandidate.getParty().getName());
    }

    @Override
    public String toString() {
        return this.party.toString();
    }

    @Override
    public int hashCode() {
        return this.party.hashCode();
    }
}
