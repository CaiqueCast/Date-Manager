# Stage de build
FROM maven:3.9.4 AS build



WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build do projeto (sem rodar testes)
RUN mvn clean package -DskipTests

# Stage final - imagem mínima e segura
FROM gcr.io/distroless/java17-debian12

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Porta dinâmica usada pelo Render
ENV JAVA_TOOL_OPTIONS="-Dserver.port=$PORT"

EXPOSE $PORT

# Entrypoint para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
