FROM openjdk:11-jdk-slim
ARG jar
VOLUME /tmp
COPY $jar censusjobsvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar /censusjobsvc.jar" ]
