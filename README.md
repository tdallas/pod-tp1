# Thread-safe remote voting system

## Deploy
`./deploy.sh <deploy-directory>`
## Run server
1. Run registry\
`<deploy-directory>/server/run-registry.sh`
2. Run server in new terminal\
`<deploy-directory>/server/run-server.sh`
## Run clients
1. Administration client\
`<deploy-directory>/client/run-management.sh -DserverAddress=xx.xx.xx.xx:yyyy -Daction=actionName`
2. Voting client\
`<deploy-directory>/client/run-vote -DserverAddress=xx.xx.xx.xx:yyyy -DvotesPath=fileName`
3. Fiscalization client\
`<deploy-directory>/client/run-fiscal -DserverAddress=xx.xx.xx.xx:yyyy -Did=pollingPlaceNumber
 -Dparty=partyName`
4. Consulting client\
`<deploy-directory>/client/run-query -DserverAddress=xx.xx.xx.xx:yyyy [-Dstate=stateName |
 -Did=pollingPlaceNumber] -DoutPath=fileName`
