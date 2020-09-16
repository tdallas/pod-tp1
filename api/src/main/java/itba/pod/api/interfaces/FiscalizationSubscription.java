package itba.pod.api.interfaces;

import java.io.Serializable;
import java.rmi.Remote;

public interface FiscalizationSubscription extends Remote, Serializable {
    void post(final String notification);
    void consume();
    void end();
}
