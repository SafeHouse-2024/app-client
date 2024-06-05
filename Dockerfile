FROM openjdk:17-jdk
WORKDIR /app

ADD ./target/app-client-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar

ENTRYPOINT ["java", "-jar","app.jar"]
