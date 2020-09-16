package itba.pod.client;

import itba.pod.api.interfaces.FiscalizationSubscription;

public class FiscalizationSubscriptionImpl implements FiscalizationSubscription {
    private boolean subscriptionIsActive = true;

    @Override
    public void post(final String notification) {
        if (subscriptionIsActive)
            System.out.println(notification);
    }

    @Override
    public void end() {
        this.subscriptionIsActive = false;

        System.out.println("The election polling places have closed");
    }
}
