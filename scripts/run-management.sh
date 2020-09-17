#!/bin/bash

serverAddress=$1
action=$2
java "$serverAddress" "{$action}" -cp client.jar itba.pod.client.AdministrationClient
