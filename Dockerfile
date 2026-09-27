FROM maven:3.9.16-eclipse-temurin-25 AS build

WORKDIR /workspace

COPY libs/cpf-validator-spring-boot-1.0.0.jar /tmp/cpf-validator.jar
RUN mvn -B org.apache.maven.plugins:maven-install-plugin:3.1.4:install-file \
    -Dfile=/tmp/cpf-validator.jar \
    -DgroupId=com.ifsp.edu \
    -DartifactId=cpf-validator-spring-boot \
    -Dversion=1.0.0 \
    -Dpackaging=jar \
    -DgeneratePom=true

COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-jre

WORKDIR /app
COPY --from=build /workspace/target/demo-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]