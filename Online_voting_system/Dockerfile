# ====== Builder stage ======
FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ====== Runner stage ======
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/OnlineVotingSystem-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "app.jar"]
