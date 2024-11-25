#FROM openjdk:17-jre-slim
FROM ghcr.io/graalvm/graalvm-community:17

WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

