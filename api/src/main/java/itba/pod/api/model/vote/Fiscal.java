package itba.pod.api.model.vote;

import itba.pod.api.interfaces.FiscalizationSubscription;

import java.io.Serializable;

public class Fiscal implements Serializable {
    private final Party party;
    private final FiscalizationSubscription subscription;

    public Fiscal(final Party party, final FiscalizationSubscription subscription) {
        this.party = party;
        this.subscription = subscription;
    }

    public Fiscal(final Party party) {
        this.party = party;
        this.subscription = null;
    }

    public Party getParty() {
        return this.party;
    }

    public FiscalizationSubscription getSubscription() {
        return this.subscription;
    }
}
