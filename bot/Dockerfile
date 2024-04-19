FROM maven:3.9.6-amazoncorretto-21-debian AS builder

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY .git /app/.git
COPY bot/pom.xml /app/bot/pom.xml
COPY bot/src/main /app/bot/src/main

RUN mvn -f bot/pom.xml package

FROM openjdk:21-jdk-slim

COPY --from=builder /app/bot/target/bot.jar /app/bot.jar
