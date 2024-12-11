FROM openjdk:23
WORKDIR webfutebol
COPY target/futebol-0.0.1-SNAPSHOT.jar futebol.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "futebol.jar"]
