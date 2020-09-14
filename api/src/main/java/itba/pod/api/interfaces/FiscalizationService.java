package itba.pod.api.interfaces;

import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Candidate;

import java.beans.PropertyChangeListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalizationService extends Remote {
    String register(final long tableId, final Candidate fiscal) throws RemoteException, ElectionException;

    public void addPropertyChangeListener(final PropertyChangeListener listener);
    }
