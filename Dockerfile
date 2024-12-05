FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

COPY ./app/pom.xml .
COPY ./app/src ./src
COPY ./app/mvnw .

RUN chmod +x ./mvnw
RUN mvn dependency:go-offline

ENV SPRING_PROFILES_ACTIVE=dev
ENV SPRING_DEVTOOLS_RESTART_ENABLED=true
ENV SPRING_DEVTOOLS_LIVERELOAD_ENABLED=true
ENV JAVA_OPTS="-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005'"

CMD ["mvn", "spring-boot:run"]
