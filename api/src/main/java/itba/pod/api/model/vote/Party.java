package itba.pod.api.model.vote;

import java.io.Serializable;
import java.util.Objects;

public class Party implements Serializable {
    private final String name;

    public Party(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object otherParty) {
        if (this == otherParty) return true;
        if (otherParty == null || getClass() != otherParty.getClass()) return false;

        Party party = (Party) otherParty;

        return Objects.equals(name, party.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}