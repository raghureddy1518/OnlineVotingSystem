FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/OnlineVotingSystem-0.0.1-SNAPSHOT.jar app.jar

# Set the port to 8080 via JVM argument
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "/app.jar"]
EXPOSE 8080