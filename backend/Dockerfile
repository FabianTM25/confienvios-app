<<<<<<< HEAD
FROM maven:3.9.9-eclipse-temurin-17 AS build
=======
# Stage 1: Compilar Spring Boot (contexto es backend/)
FROM maven:3.9.9-eclipse-temurin-17 AS backend-build
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

<<<<<<< HEAD
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
=======
# Stage 2: Imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f
