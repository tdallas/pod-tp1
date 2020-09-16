package itba.pod.api.model.vote;

import java.io.Serializable;

public class State implements Serializable {
    private String state;

    public State() {}
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
