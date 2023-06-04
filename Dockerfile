# Start with a base image
FROM amazoncorretto:11-alpine-jdk
# Copy the executable JAR file to the container
COPY currency-converter-server/target/currency-converter-*.jar /usr/local/lib/currency-converter.jar
# Expose the port that the Spring Boot application will listen on
EXPOSE 8080
# Set the command to run the application when the container starts
CMD ["java", "-jar", "/usr/local/lib/currency-converter.jar"]