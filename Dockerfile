# Build stage
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean verify

# Package stage
FROM eclipse-temurin:21-alpine
COPY --from=build ./target/*.jar app.jar
COPY ./src/main/resources src/main/resources
CMD ["java", "-jar", "app.jar"]