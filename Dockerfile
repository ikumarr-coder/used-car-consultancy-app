# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw -B dependency:go-offline -DskipTests

COPY src src
RUN ./mvnw -B -DskipTests package

# Run stage
FROM eclipse-termurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/used-car-consultancy-1.0.0.jar app.jar

ENV PORT=8080
EXPOSE 9-9-

CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]