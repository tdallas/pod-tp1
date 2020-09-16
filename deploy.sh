#!/bin/bash

mkdir -p deploy && \
mkdir -p deploy/lib/jars && \
mvn clean install -DskipTests=true && \
cd deploy && \
tar -xzf ../server/target/pod-tp1-server-1.0-SNAPSHOT.tar.gz && \
tar -xzf ../client/target/pod-tp1-client-1.0-SNAPSHOT.tar.gz && \
mv pod-tp1-server-1.0-SNAPSHOT/lib/jars/* lib/jars && \
mv pod-tp1-client-1.0-SNAPSHOT/lib/jars/* lib/jars