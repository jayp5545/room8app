FROM openjdk:17-jdk-alpine
VOLUME /backend/data
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.properties application.properties
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "-Dspring.config.location=file:application.properties", "app.jar"]