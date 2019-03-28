FROM openjdk:11-jdk
ARG jar
RUN groupadd -g 997 censusjobsvc && \
    useradd -r -u 997 -g censusjobsvc censusjobsvc
USER censusjobsvc
COPY $jar /opt/censusjobsvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "java",  "-jar", "/opt/censusjobsvc.jar" ]
