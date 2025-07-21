FROM openjdk:21-jdk-slim 
ARG JAR_FILE=target/transporte-zbuss-0.0.1.jar 
COPY ${JAR_FILE} app_zbuss.jar 
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "app_zbuss.jar"]