
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copiando arquivos do projeto
COPY pom.xml .
COPY src ./src

# Build do projeto (ignora testes para agilizar)
RUN mvn clean package -DskipTests

# Stage final - imagem leve
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiando JAR do build
COPY --from=build /app/target/*.jar app.jar

# Porta dinâmica do Render
ENV PORT 10000
ENV JAVA_TOOL_OPTIONS="-Dserver.port=$PORT"

EXPOSE $PORT

# Comando para rodar o Spring Boot
ENTRYPOINT ["java","-jar","app.jar"]
