FROM openjdk:21
LABEL authors="alexspeal"

COPY ./scrapper/target/scrapper.jar scrapper.jar

ENTRYPOINT ["java","-jar","/scrapper.jar"]
