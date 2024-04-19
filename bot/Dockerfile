FROM maven:3.8.6 AS builder

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY models/pom.xml /app/models/pom.xml
COPY models/src/main /app/models/src/main
COPY .git /app/.git
COPY bot/pom.xml /app/bot/pom.xml
COPY bot/src/main /app/bot/src/main


RUN mvn -f bot/pom.xml package

FROM openjdk:21-jdk-slim

COPY --from=builder /app/bot/target/bot.jar /app/bot.jar
