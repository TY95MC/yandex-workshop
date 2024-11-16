FROM amazoncorretto:21-alpine-jdk
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9291
COPY target/*.jar masterskaya.jar
ENTRYPOINT ["java","-jar","/masterskaya.jar"]