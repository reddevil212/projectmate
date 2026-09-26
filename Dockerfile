# Multi-stage Dockerfile for Spring Boot deployment on Render

# Step 1: Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy Gradle wrapper and configuration files first for Docker layer caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Make wrapper executable
RUN chmod +x gradlew

# Resolve dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy source files and build executable jar
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# Step 2: Production Runtime Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built app.jar from build stage
COPY --from=build /app/build/libs/app.jar app.jar

# Render dynamically sets PORT environment variable
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
