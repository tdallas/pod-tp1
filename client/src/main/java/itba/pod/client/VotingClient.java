package itba.pod.client;

import itba.pod.api.interfaces.VotingService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.vote.Vote;
import itba.pod.parser.VotesParser;
import itba.pod.server.services.VotingServiceImpl;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import static java.lang.System.exit;

public class VotingClient {

    public static void main(String[] args) {
        String serverAddress = System.getProperty("serverAddress");
        String votesPath = System.getProperty("votesPath");

        try {
            final VotingService service = (VotingServiceImpl) Naming.lookup("//" + serverAddress + "/voting-service");
            final VotesParser votesParser = new VotesParser();
            List<Vote> votes = votesParser.parse(votesPath);

            votes.parallelStream().forEach(vote -> {
                try {
                    service.emitVote(vote);
                } catch (RemoteException | ElectionException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        catch (NotBoundException | IOException e) {
            System.out.println(e.getMessage());
            exit(-1);
        }
    }
}