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

# Dockerfile under /server
That dockerfile is used for testing purpose. We simulate an env where the service runs on another host in the network. It could also be used with port forwarding as a local host.

To do so, first, you need to build the **Dockerfile**.

In root directory:

`cd server && docker build -t pod-tp1 .`

Then run image:
`docker run --publish 1099:1099 --detach --name pod-server pod-tp1`
This will run the server forwarding 1099 port (server port), so this acts as running it from a local terminal.

To run it as a remote host, you should not forward the port:
`docker run --detach --name pod-server pod-tp1`

To run some client against this server, you have to know the container's ip, to do so:
`sudo docker inspect pod-server | grep "IPAddress"`


