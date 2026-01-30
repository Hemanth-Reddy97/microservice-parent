FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY core ./core
COPY productMicroservice ./productMicroservice
COPY EmailNotificationService ./EmailNotificationService
COPY mockservice ./mockservice

RUN mvn -pl mockservice -am clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/mockservice/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
