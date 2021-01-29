FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
RUN mkdir -p /oauth-server
ADD ${JAR_FILE} /oauth-server/app.jar
EXPOSE 8080
ENV data.dir=/oauth-server/data
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/oauth-server/app.jar"]
CMD []