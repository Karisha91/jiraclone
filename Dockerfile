
# Stage 1: Build stage - we use Maven + Java 21 to compile our app
FROM maven:3.9.6-eclipse-temurin-21 AS build
# Set the working directory inside the container - all commands run from here
WORKDIR /app
# Copy pom.xml first - Docker caches this layer so dependencies
# don't re-download every time we rebuild (only when pom.xml changes)
COPY pom.xml .
# Download all Maven dependencies
RUN mvn dependency:go-offline
# Copy the rest of the source code into the container
COPY src ./src
# Build the app and skip tests (tests slow down the build)
RUN mvn clean package -DskipTests


# Stage 2: Run stage - we only need Java to RUN the app, not Maven
FROM eclipse-temurin:21-jre AS runtime
# Set working directory for the runtime container
WORKDIR /app
# Copy ONLY the compiled JAR from the build stage - keeps image small
COPY --from=build /app/target/*.jar app.jar

# Tell Docker this container listens on port 8080
EXPOSE 8080


# The command that runs when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]