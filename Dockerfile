FROM openjdk:17

ARG JAR_FILE=build/libs/geeks-0.0.1-SNAPSHOT.jar
ARG AGENT_FILE=src/main/resources/dd-java-agent.jar

COPY ${JAR_FILE} /app.jar
COPY ${AGENT_FILE} /agent.jar

#ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java","-javaagent:/agent.jar","-Ddd.logs.injection=true","-Ddd.service=geeks","-jar","/app.jar"]