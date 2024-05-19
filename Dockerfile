FROM openjdk:17-jdk-slim-buster
WORKDIR /app
ADD target/app-client-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
