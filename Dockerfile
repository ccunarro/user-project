FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the executable jar file from the host to the container
COPY target/user-project-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
