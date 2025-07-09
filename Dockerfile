FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/contractor-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
