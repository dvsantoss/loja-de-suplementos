# Build (Compila o código)
FROM maven:3.9-eclipse-temurin-24 AS build
COPY src /app/src
COPY pom.xml /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Run (Roda o aplicativo)
FROM eclipse-temurin:24-jre-alpine
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]