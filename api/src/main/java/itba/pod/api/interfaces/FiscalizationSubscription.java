package itba.pod.api.interfaces;

import java.rmi.Remote;

public interface FiscalizationSubscription extends Remote {
    void consume(final String notification);
}
