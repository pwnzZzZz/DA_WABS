# Use Maven OpenJDK for base image.
FROM maven:3.8.1-openjdk-17 as builder

WORKDIR /usr/app

# Copy the pom.xml file
COPY ./pom.xml ./

# Download all required dependencies into one layer
RUN mvn dependency:go-offline -B

# Copy your other files
COPY ./src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Start a new stage for a minimal runtime image
FROM openjdk:17-jdk-slim as runtime

WORKDIR /usr/app

# Copy the jar file from the builder stage
COPY --from=builder /usr/app/target/wabs.jar .

# Set the startup command to run your binary
CMD ["java", "-jar", "./wabs.jar"]
