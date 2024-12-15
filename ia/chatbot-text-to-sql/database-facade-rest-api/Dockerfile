# Runtime stage
FROM eclipse-temurin:21-jre-jammy

# Add maintainer info
LABEL maintainer="your-email@example.com"

# Set working directory
WORKDIR /app

# Create non-root user
RUN useradd -r -u 1001 -g root springuser

# Set environment variables
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Copy JAR file from local machine to container
COPY target/*.jar app.jar

# Set ownership of the application files
RUN chown springuser:root /app \
    && chmod 775 /app \
    && chown springuser:root app.jar

# Switch to non-root user
USER 1001

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
    CMD curl -f http://localhost:8080/api/v1/health || exit 1

# Run application
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]