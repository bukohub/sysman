FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY sysman-domain/pom.xml sysman-domain/pom.xml
COPY sysman-application/pom.xml sysman-application/pom.xml
COPY sysman-infrastructure-persistence/pom.xml sysman-infrastructure-persistence/pom.xml
COPY sysman-infrastructure-rest/pom.xml sysman-infrastructure-rest/pom.xml
COPY sysman-bootstrap/pom.xml sysman-bootstrap/pom.xml

COPY . .
RUN mvn -B -q -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/sysman-bootstrap/target/sysman-bootstrap.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
