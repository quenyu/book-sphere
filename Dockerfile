FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /app
COPY build.gradle settings.gradle gradle.properties ./
COPY src ./src
RUN gradle clean build -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]