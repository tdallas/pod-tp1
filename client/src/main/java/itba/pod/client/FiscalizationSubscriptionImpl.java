package itba.pod.client;

import itba.pod.api.interfaces.FiscalizationSubscription;

import java.util.LinkedList;
import java.util.Queue;

public class FiscalizationSubscriptionImpl implements FiscalizationSubscription {
    private final Queue<String> notifications = new LinkedList<>();
    private boolean subscriptionIsActive = true;

    @Override
    public void consume() {
        while (subscriptionIsActive)
            if (!this.notifications.isEmpty())
                System.out.println(this.notifications.poll());

        System.out.println("The election polling places have closed");
    }

    @Override
    public void post(final String notification) {
        this.notifications.offer(notification);
        System.out.println("Agregue una notificacion " + notifications.peek());
    }

    @Override
    public void end() {
        this.subscriptionIsActive = false;
    }
}
