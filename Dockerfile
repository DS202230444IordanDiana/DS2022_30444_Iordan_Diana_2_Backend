FROM maven:latest AS build-project

ADD . ./docker-spring-boot
WORKDIR /docker-spring-boot
RUN mvn clean install


FROM openjdk:17-alpine
EXPOSE 8080

COPY --from=build-project /docker-spring-boot/target/monitoring-platform-0.0.1-SNAPSHOT.jar ./docker-spring-boot.jar
ENTRYPOINT ["java", "-jar","./docker-spring-boot.jar"]
