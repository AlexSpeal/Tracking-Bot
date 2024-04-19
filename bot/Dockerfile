FROM openjdk:21
LABEL authors="alexspeal"

COPY ./bot/target/bot.jar bot.jar

ENTRYPOINT ["java","-jar","/bot.jar"]
