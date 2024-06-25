# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# Add Maintainer Info
LABEL maintainer="en.snnaydin@gmail.com"

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container at path /app
COPY target/application-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
