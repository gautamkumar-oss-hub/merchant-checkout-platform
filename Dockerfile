FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY target/checkout-platform-0.1.0.jar /app/checkout-platform.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/checkout-platform.jar"]
