# Etapa 1: Construcción con Gradle
FROM gradle:8.11.1-jdk17 AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Instalar Node.js y Yarn (necesarios para wasmJs)
RUN apt-get update && apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs yarn && \
    rm -rf /var/lib/apt/lists/*

# Copiar archivos de configuración de Gradle primero (para aprovechar cache de Docker)
COPY gradlew gradle.properties settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

# Dar permisos de ejecución al wrapper
RUN chmod +x ./gradlew

# Copiar el resto del código fuente
COPY composeApp ./composeApp

# Construir la aplicación WASM para producción usando el wrapper
RUN ./gradlew :composeApp:wasmJsBrowserDistribution --no-daemon --no-configuration-cache

# Etapa 2: Servidor nginx para servir la aplicación
FROM nginx:alpine

# Instalar envsubst para variables de entorno
RUN apk add --no-cache gettext

# Copiar los archivos compilados desde la etapa de construcción
COPY --from=builder /app/composeApp/build/dist/wasmJs/productionExecutable /usr/share/nginx/html

# Copiar configuración de nginx
COPY nginx.conf.template /etc/nginx/templates/default.conf.template

# Script de inicio personalizado
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# Exponer el puerto (Railway lo sobreescribirá con su variable PORT)
EXPOSE 80

# Usar el script de inicio personalizado
ENTRYPOINT ["/docker-entrypoint.sh"]
