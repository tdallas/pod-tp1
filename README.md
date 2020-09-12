# pod-tp1

`mvn clean install`

# Dockerfile under /server
That dockerfile is used for test purpose. We simulate an env where the service runs on another host in the network. It could also be used with port forwarding as a local host.

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


