#!/bin/bash

echo "🚀 Iniciando despliegue de Kotlin/WASM en Railway..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar Railway CLI
if ! command -v railway &> /dev/null; then
    echo -e "${RED}❌ Railway CLI no está instalado${NC}"
    echo "Instala Railway CLI desde: https://docs.railway.app/develop/cli"
    exit 1
fi

# Verificar si está logueado en Railway
if ! railway whoami &> /dev/null; then
    echo -e "${YELLOW}⚠️ No estás logueado en Railway${NC}"
    echo "Ejecutando railway login..."
    railway login
fi

# Construir localmente para verificar
echo -e "${YELLOW}📦 Construyendo aplicación localmente...${NC}"
./gradlew :composeApp:wasmJsBrowserDistribution

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error en la construcción local${NC}"
    exit 1
fi

# Verificar que los archivos se generaron
if [ ! -d "composeApp/build/dist/wasmJs/productionExecutable" ]; then
    echo -e "${RED}❌ No se encontraron los archivos compilados${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Construcción local exitosa${NC}"

# Listar archivos generados
echo -e "${YELLOW}📋 Archivos generados:${NC}"
ls -la composeApp/build/dist/wasmJs/productionExecutable/

# Verificar archivos Docker necesarios
echo -e "${YELLOW}🔍 Verificando archivos Docker...${NC}"

files_to_check=("Dockerfile" "nginx.conf.template" "docker-entrypoint.sh")
for file in "${files_to_check[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✅ $file existe${NC}"
    else
        echo -e "${RED}❌ $file no encontrado${NC}"
        exit 1
    fi
done

# Hacer ejecutable el script de entrada
chmod +x docker-entrypoint.sh

# Desplegar en Railway
echo -e "${YELLOW}🚂 Desplegando en Railway...${NC}"
railway up

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Despliegue exitoso!${NC}"
    echo -e "${YELLOW}📌 Para ver los logs: railway logs${NC}"
    echo -e "${YELLOW}🌐 Para abrir la app: railway open${NC}"
else
    echo -e "${RED}❌ Error en el despliegue${NC}"
    echo -e "${YELLOW}💡 Verifica los logs con: railway logs${NC}"
fi