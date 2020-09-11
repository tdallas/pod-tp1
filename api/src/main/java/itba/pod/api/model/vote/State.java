package itba.pod.api.model.vote;

import java.util.Objects;

public class State {
    private final String state;

    public State(final String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state1 = (State) o;
        return state.equals(state1.state);
    }
}
