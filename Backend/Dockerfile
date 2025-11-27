FROM eclipse-temurin:22-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/culinairy-backend.jar backend.jar
EXPOSE 1010
ENTRYPOINT ["java","-jar","/backend.jar"]
