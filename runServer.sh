#!/bin/bash

mvn clean install -DskipTests=true && \
   java -jar server/target/pod-tp1-server-1.0-SNAPSHOT.jar -Djava.rmi.server.hostname=localhost && \
    ./runSomeVotes.sh