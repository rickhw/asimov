# don't use openjdk:17- 
# FROM openjdk:17-jdk-slim

FROM eclipse-temurin:17-jdk-jammy
# FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# COPY build/libs/asimov-webapi-1.0.0.jar app.jar
COPY build/libs/*.jar app.jar

# 修正 ENTRYPOINT 的語法
ENTRYPOINT ["java", "-jar", "app.jar"]