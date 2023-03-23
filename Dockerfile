FROM maven:3.8.6-openjdk-18 AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
COPY images /workspace/images
RUN mvn -f pom.xml clean package

FROM openjdk:18-alpine
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]