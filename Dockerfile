# Use a base image with JDK and Gradle
FROM gradle:8.5-jdk17 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy only the gradle wrapper files and build scripts first (for caching)
COPY gradle gradle
COPY gradlew build.gradle settings.gradle /app/

# Download Gradle dependencies early
RUN ./gradlew build -x test --no-daemon

# Now copy the full source
COPY . .

# Run the tests
RUN ./gradlew test --no-daemon

# Optional: default command
CMD ["./gradlew", "test"]
