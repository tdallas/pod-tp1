package itba.pod.client;


import itba.pod.api.model.election.Results;
import itba.pod.api.model.election.Status;
import itba.pod.api.model.vote.Candidate;
import itba.pod.api.model.vote.State;
import itba.pod.api.model.vote.Table;

import java.io.FileWriter;
import java.io.IOException;
import java.security.Provider;
import java.util.LinkedList;
import java.util.List;

public class ConsultingClient {
    private Table table;
    private State state;

    private void CSVResults(Results results, String path) throws IOException, ClientException {
        int provinceRounds=3;
        if (results.getStatus() == Status.NOT_INITIALIZED) {
            throw new ClientException("Election not initialize");
        }
        try (FileWriter fw = new FileWriter(path)) {
            StringBuilder sb = new StringBuilder();
            if (results.getStatus() == Status.INITIALIZED) {
                sb.append("Percentage;Party\n");
                results.getWinners().get(0).forEach((k, v) -> sb.append(String.format("%.2f", v)).append("%;").append(k).append("\n"));
            } else {
                if (state == null && table == null) { //nacional
                    sb.append("Score;Party\n");
                    results.getWinners().get(0).forEach((k, v) -> sb.append(v).append("%;").append(k).append("\n"));
                    sb.append("Percentage;Party\n");
                    results.getWinners().get(1).forEach((k, v) -> sb.append(String.format("%.2f", v)).append("%;").append(k).append("\n"));
                    sb.append("Winner\n");
                    sb.append(results.getWinners().get(1).entrySet().iterator().next().getKey()).append("\n");
                } else if (table == null) {//provincial
                    List<Candidate> aux=new LinkedList<>();
                    for(int i=0;i<provinceRounds;i++){
                        sb.append("Round ").append(i+1).append("\n");
                        sb.append("Approval;Party\n");
                        results.getWinners().get(i).forEach((k, v) -> sb.append(String.format("%.2f", v)).append(";").append(k).append("\n"));
                        sb.append("Winners\n");
                        aux.add(results.getWinners().get(i).entrySet().iterator().next().getKey());
                        sb.append(aux).append("\n");
                    }
                }
                else{
                    sb.append("Percentage;Party\n");
                    results.getWinners().get(0).forEach((k, v) -> sb.append(String.format("%.2f", v)).append("%;").append(k).append("\n"));
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

