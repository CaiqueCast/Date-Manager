# Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Final stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# EXPOSE é informativo
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
