# Stage 1: Compilar Spring Boot (contexto es backend/)
FROM maven:3.9.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]