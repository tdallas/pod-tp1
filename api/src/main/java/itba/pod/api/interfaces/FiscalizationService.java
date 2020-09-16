package itba.pod.api.interfaces;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Fiscal;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalizationService extends Remote {
    String register(final long tableId, final Fiscal fiscal) throws RemoteException, ElectionException;
}
