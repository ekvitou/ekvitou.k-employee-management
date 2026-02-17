# download jdk for container
FROM eclipse-temurin:21-jre-alpine
# create project folder inside the container
WORKDIR /app
# copy the jar file from build output
COPY build/libs/*.jar app.jar
# run the application
ENTRYPOINT ["java", "-jar", "app.jar"]