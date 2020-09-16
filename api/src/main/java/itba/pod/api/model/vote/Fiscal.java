package itba.pod.api.model.vote;

import itba.pod.api.interfaces.FiscalizationSubscription;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Fiscal implements Serializable {
    private final Party party;
    private final FiscalizationSubscription subscription;

    public Fiscal(final Party party, final FiscalizationSubscription subscription) {
        this.party = party;
        this.subscription = subscription;
    }

    public Party getParty() {
        return this.party;
    }

    public void notifyVote(final String newVoteNotification) throws RemoteException { this.subscription.post(newVoteNotification); }

    public void endSubscription() throws RemoteException {
        this.subscription.end();
    }
}
