package itba.pod.server.services;

import itba.pod.api.interfaces.FiscalizationServiceInterface;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.Table;
import itba.pod.server.elections.Election;

import java.rmi.RemoteException;

public class FiscalizationService implements FiscalizationServiceInterface {
    private Election election;

    public FiscalizationService(Election election) {
        this.election = election;
    }

    @Override
    public void register(Table table, Candidate fiscal) throws RemoteException, ElectionException {
        if (election.getStatus() == Status.INITIALIZED || election.getStatus() == Status.FINISHED) {
            // TODO: Change this to a reasonable exception
            throw new ElectionException("No new fiscal");
        }


    }

    @Override
    public String notifyFiscal(Table table, Candidate fiscal) throws RemoteException {
        return "New vote for " + fiscal.name() + " on polling place " + table.getId();
    }
}
