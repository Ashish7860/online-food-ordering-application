# Use an official OpenJDK image as the base image
FROM openjdk:17-alpine

# Expose the port the application runs on
EXPOSE 8081

# Copy the application jar file into the container
ADD target/*.jar online-food-ordering-system.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/online-food-ordering-system.jar"]
