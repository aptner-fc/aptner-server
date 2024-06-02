FROM openjdk:17
ARG JAR_FILE=client/build/libs/client-api.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]