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

# Create nginx config using RUN command
RUN cat > /etc/nginx/conf.d/default.conf <<'EOF'
server {
    listen 8080;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Force MIME type for WASM files
    location ~* \.wasm$ {
        add_header Content-Type application/wasm;
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    location ~* \.js$ {
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    location / {
        try_files $uri $uri/ /index.html;
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }

    # Enable gzip compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_comp_level 6;
    gzip_types 
        text/plain 
        text/css 
        text/xml 
        text/javascript 
        application/javascript 
        application/xml+rss 
        application/json 
        application/wasm;
}
EOF

# Create startup script to use Railway's PORT variable
RUN cat > /docker-entrypoint.sh <<'EOF'
#!/bin/sh
# Replace 8080 with Railway's PORT if available
if [ -n "$PORT" ]; then
    sed -i "s/listen 8080;/listen $PORT;/g" /etc/nginx/conf.d/default.conf
fi
exec nginx -g "daemon off;"
EOF

RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080

CMD ["/docker-entrypoint.sh"]