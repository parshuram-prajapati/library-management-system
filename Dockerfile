# Use Java 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy project
COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build Spring Boot app
RUN ./mvnw clean package -DskipTests

# Rename the jar to app.jar
RUN mv target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run app
CMD ["java", "-jar", "app.jar"]
