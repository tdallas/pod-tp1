FROM anapsix/alpine-java
MAINTAINER pod-tp1
COPY server/target/pod-tp1-server-1.0-SNAPSHOT.jar pod-tp1-server-1.0-SNAPSHOT.jar
CMD ["java","-jar","-Djava.rmi.server.hostname=localhost", "pod-tp1-server-1.0-SNAPSHOT.jar"]
