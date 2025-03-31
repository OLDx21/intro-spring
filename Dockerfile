FROM openjdk:21-jdk-slim
ENV JAVA_OPTS="-Xmx2g -Xms1g -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
ENV spring.profiles.active="local"
ADD ./target/*.jar intro-spring.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "intro-spring.jar"]
