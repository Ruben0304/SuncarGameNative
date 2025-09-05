# Build stage
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy Gradle wrapper files first
COPY gradle gradle
COPY gradle/wrapper gradle/wrapper
COPY gradlew gradlew.bat gradle.properties settings.gradle.kts ./
COPY build.gradle.kts ./
COPY composeApp/build.gradle.kts composeApp/

# Copy source
COPY composeApp/ composeApp/

# Make gradlew executable
RUN chmod +x ./gradlew

# Build only wasm target with full distribution
RUN ./gradlew :composeApp:wasmJsBrowserDistribution -PwasmBuild=true --no-daemon --stacktrace

# Serve with Nginx
FROM nginx:alpine

# Copy built app
COPY --from=builder /app/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/

# Add WASM MIME type to nginx
RUN echo 'application/wasm wasm;' >> /etc/nginx/mime.types

# Create nginx config file
RUN echo 'server {' > /etc/nginx/conf.d/default.conf && \
    echo '    listen 8080;' >> /etc/nginx/conf.d/default.conf && \
    echo '    server_name localhost;' >> /etc/nginx/conf.d/default.conf && \
    echo '    root /usr/share/nginx/html;' >> /etc/nginx/conf.d/default.conf && \
    echo '    index index.html;' >> /etc/nginx/conf.d/default.conf && \
    echo '' >> /etc/nginx/conf.d/default.conf && \
    echo '    location ~* \.wasm$ {' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Content-Type application/wasm;' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cross-Origin-Embedder-Policy require-corp;' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cross-Origin-Opener-Policy same-origin;' >> /etc/nginx/conf.d/default.conf && \
    echo '        expires 1y;' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cache-Control "public, immutable";' >> /etc/nginx/conf.d/default.conf && \
    echo '    }' >> /etc/nginx/conf.d/default.conf && \
    echo '' >> /etc/nginx/conf.d/default.conf && \
    echo '    location ~* \.js$ {' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cross-Origin-Embedder-Policy require-corp;' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cross-Origin-Opener-Policy same-origin;' >> /etc/nginx/conf.d/default.conf && \
    echo '        expires 1y;' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cache-Control "public, immutable";' >> /etc/nginx/conf.d/default.conf && \
    echo '    }' >> /etc/nginx/conf.d/default.conf && \
    echo '' >> /etc/nginx/conf.d/default.conf && \
    echo '    location / {' >> /etc/nginx/conf.d/default.conf && \
    echo '        try_files $uri $uri/ /index.html;' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cross-Origin-Embedder-Policy require-corp;' >> /etc/nginx/conf.d/default.conf && \
    echo '        add_header Cross-Origin-Opener-Policy same-origin;' >> /etc/nginx/conf.d/default.conf && \
    echo '    }' >> /etc/nginx/conf.d/default.conf && \
    echo '' >> /etc/nginx/conf.d/default.conf && \
    echo '    gzip on;' >> /etc/nginx/conf.d/default.conf && \
    echo '    gzip_vary on;' >> /etc/nginx/conf.d/default.conf && \
    echo '    gzip_min_length 1024;' >> /etc/nginx/conf.d/default.conf && \
    echo '    gzip_comp_level 6;' >> /etc/nginx/conf.d/default.conf && \
    echo '    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json application/wasm;' >> /etc/nginx/conf.d/default.conf && \
    echo '}' >> /etc/nginx/conf.d/default.conf

# Create startup script
RUN echo '#!/bin/sh' > /docker-entrypoint.sh && \
    echo 'if [ -n "$PORT" ]; then' >> /docker-entrypoint.sh && \
    echo '    sed -i "s/listen 8080;/listen $PORT;/g" /etc/nginx/conf.d/default.conf' >> /docker-entrypoint.sh && \
    echo 'fi' >> /docker-entrypoint.sh && \
    echo 'exec nginx -g "daemon off;"' >> /docker-entrypoint.sh && \
    chmod +x /docker-entrypoint.sh

EXPOSE 8080

CMD ["/docker-entrypoint.sh"]