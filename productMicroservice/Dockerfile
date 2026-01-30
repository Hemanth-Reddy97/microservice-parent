FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent POM + ALL modules (required for Maven)
COPY pom.xml .
COPY core ./core
COPY productMicroservice ./productMicroservice
COPY EmailNotificationService ./EmailNotificationService
COPY mockservice ./mockservice

# Build only this service and its dependencies
RUN mvn -pl productMicroservice -am clean package -DskipTests

# ---- Runtime image ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/productMicroservice/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
