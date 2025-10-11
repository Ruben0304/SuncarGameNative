#!/bin/sh
set -e

# Si no se proporciona PORT, usar 80 por defecto
if [ -z "$PORT" ]; then
    export PORT=80
fi

echo "Starting nginx on port $PORT"

# Reemplazar variables de entorno en la configuración de nginx
envsubst '${PORT}' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf

# Verificar que los archivos WASM existen
if [ ! -f "/usr/share/nginx/html/index.html" ]; then
    echo "ERROR: index.html not found in /usr/share/nginx/html"
    exit 1
fi

# Listar archivos WASM para verificación
echo "WASM files found:"
ls -la /usr/share/nginx/html/*.wasm 2>/dev/null || echo "No WASM files found"

# Iniciar nginx en primer plano
exec nginx -g 'daemon off;'