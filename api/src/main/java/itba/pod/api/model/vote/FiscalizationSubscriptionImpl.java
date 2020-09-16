package itba.pod.api.model.vote;

import itba.pod.api.interfaces.FiscalizationSubscription;

import java.io.Serializable;

public class FiscalizationSubscriptionImpl implements FiscalizationSubscription, Serializable {
    @Override
    public void consume(String notification) {
        System.out.println(notification);
    }
}
