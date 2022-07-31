#FROM openjdk:17-ea-11-jdk-slim
#VOLUME /tmp
#COPY discoveryservice-0.0.1-SNAPSHOT.jar DiscoveryService.jar
#ENTRYPOINT ["java", "-jar", "DiscoveryService.jar"]

FROM openjdk:17-ea-11-jdk-slim AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17-ea-11-jdk-slim
COPY --from=builder build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]