# Stage 1: Compilar Angular
FROM node:18 AS frontend-build
WORKDIR /frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npx ng build --configuration=production

# Stage 2: Compilar Spring Boot con el frontend incluido
FROM maven:3.9.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY backend/ .
COPY --from=frontend-build /frontend/dist/ src/main/resources/static/
RUN mvn clean package -DskipTests

# Stage 3: Imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]