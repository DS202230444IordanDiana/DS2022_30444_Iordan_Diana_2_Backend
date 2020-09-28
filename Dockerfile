FROM maven:3.6.3-jdk-8 AS build

COPY ./src/ /root/src
COPY ./pom.xml /root/
COPY ./checkstyle.xml /root/

WORKDIR /root
RUN mvn clean install -X

FROM tomcat:8

ENV TZ=UTC
CMD  /usr/local/tomcat/bin/catalina.sh run
COPY --from=build /root/target/ds-2020-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ds-2020-0.0.1-SNAPSHOT.war

EXPOSE 8080