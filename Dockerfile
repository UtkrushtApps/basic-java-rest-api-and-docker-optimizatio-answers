# Optimized Dockerfile for Spring Boot
FROM eclipse-temurin:17-jre-alpine as base

WORKDIR /app

# Only copy the fat jar, not source code or build files
COPY target/tasktracker-0.0.1-SNAPSHOT.jar app.jar

# JVM resource limits: set heap+container-aware flags
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
