#!/bin/bash

java -DserverAddress=localhost -Daction=open -cp client/target/pod-tp1-client-1.0-SNAPSHOT.jar itba.pod.client.AdministrationClient && \
java -DserverAddress=localhost -DvotesPath=client/src/test/resources/csv_test.csv -cp client/target/pod-tp1-client-1.0-SNAPSHOT.jar itba.pod.client.VotingClient && \
java -DserverAddress=localhost -DoutPath=test.csv -cp client/target/pod-tp1-client-1.0-SNAPSHOT.jar itba.pod.client.ConsultingClient && \
cat test.csv