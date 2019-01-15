FROM openjdk:11-jdk-slim
ARG jar
COPY $jar /opt/censusjobsvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar /opt/censusjobsvc.jar" ]
