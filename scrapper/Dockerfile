FROM maven:3.9.6-amazoncorretto-21-debian AS builder

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY .git /app/.git
COPY scrapper/pom.xml /app/scrapper/pom.xml
COPY scrapper/src/main /app/scrapper/src/main
COPY models/pom.xml /app/models/pom.xml
COPY models/src/main /app/models/src/main

RUN mvn -f scrapper/pom.xml package

FROM openjdk:21-jdk-slim

COPY --from=builder /app/scrapper/target/scrapper.jar /app/scrapper.jar
