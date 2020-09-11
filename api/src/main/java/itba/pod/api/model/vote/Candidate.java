package itba.pod.api.model.vote;

import java.util.Objects;

public class Candidate {
    private final String name;

    public Candidate(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(name, candidate.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override public int hashCode() {
        return Objects.hash(name);
    }
}
