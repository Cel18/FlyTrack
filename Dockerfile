# ─── Etapa 1: Build del Frontend ──────────────────────────────────────
FROM node:20-alpine AS frontend-build

WORKDIR /app/frontend

COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci --prefer-offline

COPY frontend/ ./
RUN npm run build

# ─── Etapa 2: Build del Backend Spring Boot ────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS backend-build

WORKDIR /app

# Copiar wrapper y archivos de configuración Gradle
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle/ gradle/
RUN chmod +x gradlew

# Copiar fuentes del backend
COPY backend/ backend/

# Copiar el build del frontend al directorio de estáticos del backend
COPY --from=frontend-build /app/frontend/dist backend/src/main/resources/static/

# Construir el backend (sin la tarea copyFrontendBuild porque ya lo copiamos)
RUN ./gradlew :backend:build -x test -x copyFrontendBuild --no-daemon

# ─── Etapa 3: Imagen final de ejecución ───────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copiar solo el jar final
COPY --from=backend-build /app/backend/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
