FROM nginx:alpine

# Eliminar la página por defecto de nginx
RUN rm -rf /usr/share/nginx/html/*

# Configuración de Nginx para WASM
RUN cat > /etc/nginx/conf.d/default.conf <<'NGINX_CONFIG'
server {
    listen 8080;
    server_name _;
    root /usr/share/nginx/html;
    index index.html;
    
    # Headers necesarios para WASM
    location / {
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
        try_files $uri $uri/ /index.html;
    }
    
    # Configuración específica para archivos WASM
    location ~ \.wasm$ {
        add_header Content-Type application/wasm;
        add_header Cross-Origin-Embedder-Policy require-corp;
        add_header Cross-Origin-Opener-Policy same-origin;
    }
    
    # Compresión
    gzip on;
    gzip_types text/plain text/css text/javascript application/javascript application/wasm;
}
NGINX_CONFIG

# Copia los archivos ya compilados DESPUÉS de configurar nginx
COPY *.html /usr/share/nginx/html/
COPY *.css /usr/share/nginx/html/
COPY *.js /usr/share/nginx/html/
COPY *.wasm /usr/share/nginx/html/
COPY composeResources/ /usr/share/nginx/html/composeResources/

# Script para usar el puerto de Railway
RUN cat > /start.sh <<'SCRIPT'
#!/bin/sh
if [ "$PORT" ]; then
    sed -i "s/listen 8080;/listen $PORT;/" /etc/nginx/conf.d/default.conf
fi

# Debug: mostrar archivos copiados
echo "=== Archivos en /usr/share/nginx/html ==="
ls -la /usr/share/nginx/html/
echo "============================================"

nginx -g "daemon off;"
SCRIPT

RUN chmod +x /start.sh

EXPOSE 8080
CMD ["/start.sh"]