#!/bin/bash

if [ $# -lt 1 ]; then
    echo "You must specify the deploy directory"
    exit 1
fi

mkdir -p $1
cd "${0%/*}"
mvn clean install -DskipTests=true
tar -xzf ${0%/*}/../pod-tp1/server/target/pod-tp1-server-1.0-SNAPSHOT.tar.gz
tar -xzf ${0%/*}/../pod-tp1/client/target/pod-tp1-client-1.0-SNAPSHOT.tar.gz
mv pod-tp1-server-1.0-SNAPSHOT $1/server
mv pod-tp1-client-1.0-SNAPSHOT $1/client