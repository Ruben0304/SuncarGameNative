# Build stage
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy Gradle wrapper and settings first
COPY gradle gradle
COPY gradlew gradlew.bat gradle.properties settings.gradle.kts ./
COPY build.gradle.kts ./
COPY composeApp/build.gradle.kts composeApp/

# Copy source
COPY composeApp/ composeApp/

# Make gradlew executable
RUN chmod +x ./gradlew

# Build only wasm target
RUN ./gradlew :composeApp:wasmJsBrowserProductionWebpack -PwasmBuild=true --no-daemon --stacktrace

# Serve with Nginx
FROM nginx:alpine

# Copy built app
COPY --from=builder /app/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/

# Nginx config
COPY <<EOF /etc/nginx/conf.d/default.conf
server {
    listen 8080;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    location ~* \.wasm$ {
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }

    location ~* \.js$ {
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }

    location / {
        try_files \$uri \$uri/ /index.html;
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }

    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json application/wasm;
}
EOF

EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
