#!/bin/bash

java -cp client/target/pod-tp1-client-1.0-SNAPSHOT.jar itba.pod.client.FiscalizationClient "$1" "$2" "$3"