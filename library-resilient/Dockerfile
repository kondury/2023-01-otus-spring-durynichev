# syntax=docker/dockerfile:1

FROM bellsoft/liberica-openjdk-alpine:17.0.7 as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as test
CMD ["./mvnw", "test"]

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base as build
RUN ./mvnw package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17.0.7 as production
EXPOSE 8080
COPY --from=build /app/target/library-dockerized-*.jar /library-dockerized.jar
CMD ["java", "-jar", "/library-dockerized.jar"]