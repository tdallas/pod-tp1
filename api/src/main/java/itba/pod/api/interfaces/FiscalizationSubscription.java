package itba.pod.api.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalizationSubscription extends Remote, Serializable {
    void post(final String notification) throws RemoteException;
    void end() throws RemoteException;
}
