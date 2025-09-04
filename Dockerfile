# Multi-stage Docker build for Kotlin Multiplatform Compose Web app

# Build stage
FROM gradle:8.10.2-jdk17 AS builder

# Set working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY gradle gradle
COPY gradlew gradlew.bat gradle.properties settings.gradle.kts ./
COPY build.gradle.kts ./

# Copy source code
COPY composeApp/ composeApp/

# Set gradle daemon properties for container environment
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.workers.max=4 -Xmx2g"

# Build the WASM JS application
RUN ./gradlew :composeApp:wasmJsBrowserProductionWebpack --no-daemon --stacktrace

# Production stage - nginx for serving static files
FROM nginx:alpine

# Install Node.js for serving WASM files with proper MIME types
RUN apk add --no-cache nodejs npm

# Copy built WASM application
COPY --from=builder /app/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/

# Create nginx configuration for WASM and proper routing
RUN cat > /etc/nginx/conf.d/default.conf << 'EOF'
server {
    listen 8080;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Add WASM MIME type
    location ~* \.wasm$ {
        add_header Content-Type application/wasm;
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }

    # Handle JS files with proper headers for WASM
    location ~* \.js$ {
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }

    # Serve static files
    location / {
        try_files $uri $uri/ /index.html;
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }

    # Enable gzip compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json application/wasm;
}
EOF

# Expose port 8080 (Railway requirement)
EXPOSE 8080

# Use nginx in foreground
CMD ["nginx", "-g", "daemon off;"]