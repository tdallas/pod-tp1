package itba.pod.client;


import itba.pod.api.interfaces.ConsultingService;
import itba.pod.api.model.election.ElectionException;
import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.State;
import itba.pod.api.model.vote.Table;
import itba.pod.server.services.ConsultingServiceImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class ConsultingClient {

    public static void main(String[] args) {
        String serverAddress = System.getProperty("serverAddress");
        String stateName = System.getProperty("state");
        String pollingPlaceNumber = System.getProperty("id");
        String fileName = System.getProperty("outPath");

        if(stateName!=null && pollingPlaceNumber!=null){
            System.out.println("Invalid Params (stateName or poolingPlace");
            exit(-1);
        }
        try {
            Results result;
            final ConsultingService service  = (ConsultingServiceImpl) Naming.lookup("//" + serverAddress + "/consulting-service");
            if(stateName==null && pollingPlaceNumber==null){
                result=service.getNationalResults();
            }
            else if(stateName!=null){
                result=service.getStateResults(new State(stateName));
            }
            else {
                result=service.getTableResults(new Table(Integer.parseInt(pollingPlaceNumber)));
            }
            CSVResults(result,fileName,stateName,pollingPlaceNumber);
        }
        catch (RemoteException | NotBoundException | MalformedURLException | ElectionException e){
            System.out.println(e.getMessage());
            exit(-1);
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }

    static void CSVResults(Results results, String path, String state, String table) throws ClientException {
        int provinceRounds = 3;
        if (results.getStatus() == Status.NOT_INITIALIZED) {
            throw new ClientException("Election not initialize");
        }
        try (FileWriter fw = new FileWriter(path)) {
            StringBuilder sb = new StringBuilder();
            if (results.getStatus() == Status.INITIALIZED) {
                sb.append("Percentage;Party\n");
                results.getWinners().get(0).forEach((k, v) -> sb.append(String.format("%.2f", v)).append("%;").append(k.getName()).append("\n"));
            } else {
                if (state == null && table == null) { //nacional
                    sb.append("Score;Party\n");
                    results.getWinners().get(0).forEach((k, v) -> sb.append(v.intValue()).append(";").append(k.getName()).append("\n"));
                    sb.append("Percentage;Party\n");
                    results.getWinners().get(1).forEach((k, v) -> sb.append(String.format("%.2f", v * 100)).append("%;").append(k.getName()).append("\n"));
                    sb.append("Winner\n");
                    sb.append(results.getWinners().get(1).entrySet().iterator().next().getKey()).append("\n");
                } else if (table == null) {//provincial
                    List<Candidate> aux = new LinkedList<>();
                    for (int i = 0; i < provinceRounds; i++) {
                        sb.append("Round ").append(i + 1).append("\n");
                        sb.append("Approval;Party\n");
                        results.getWinners().get(i).forEach((k, v) -> sb.append(String.format("%.2f", v)).append(";").append(k.getName()).append("\n"));
                        sb.append("Winners\n");
                        aux.add(results.getWinners().get(i).entrySet().iterator().next().getKey());
                        for (Candidate c : aux) {
                            sb.append(c);
                            if (c != aux.get(aux.size() - 1))
                                sb.append(",");
                        }
                        sb.append("\n");
                    }
                } else {
                    sb.append("Percentage;Party\n");
                    results.getWinners().get(0).forEach((k, v) -> sb.append(String.format("%.2f", v * 100)).append("%;").append(k.getName()).append("\n"));
                    sb.append("Winner\n");
                    sb.append(results.getWinners().get(0).entrySet().iterator().next().getKey()).append("\n");

                }
            }
            fw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();//todo handle error
        }
    }
}

