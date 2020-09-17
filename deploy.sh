#!/bin/bash

deployDir=$1

mkdir -p "$deployDir" && \
mkdir -p "$deployDir"/lib/jars && \
mvn clean install -DskipTests=true && \
cd "$deployDir" && \
cp -r ../scripts/* . && \
tar -xzf ../server/target/pod-tp1-server-1.0-SNAPSHOT.tar.gz && \
tar -xzf ../client/target/pod-tp1-client-1.0-SNAPSHOT.tar.gz && \
mv pod-tp1-server-1.0-SNAPSHOT/lib/jars/* lib/jars && \
mv pod-tp1-client-1.0-SNAPSHOT/lib/jars/* lib/jars && \
ln -s lib/jars/pod-tp1-server-1.0-SNAPSHOT.jar server.jar && \
ln -s lib/jars/pod-tp1-client-1.0-SNAPSHOT.jar client.jar