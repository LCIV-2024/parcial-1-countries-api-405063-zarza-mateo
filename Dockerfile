FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/*.jar paises-app.jar
ENTRYPOINT ["java","-jar","paises-app.jar"]