FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

FROM amazoncorretto:21.0.4-alpine3.18
WORKDIR /app
COPY --from=build /app/target/dimuapi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]