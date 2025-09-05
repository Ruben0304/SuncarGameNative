#!/bin/bash

# Compila el proyecto WASM
echo "🔨 Compilando proyecto WASM..."
./gradlew :composeApp:wasmJsBrowserProductionWebpack

# Limpia el directorio de despliegue
echo "🧹 Limpiando directorio de despliegue..."
rm -rf railway-deploy/*

# Copia los archivos compilados
echo "📦 Copiando archivos compilados..."

# Copia archivos HTML y CSS desde processedResources
cp composeApp/build/processedResources/wasmJs/main/*.html railway-deploy/
cp composeApp/build/processedResources/wasmJs/main/*.css railway-deploy/
cp -r composeApp/build/processedResources/wasmJs/main/composeResources railway-deploy/

# Copia archivos JS y WASM desde kotlin-webpack
cp composeApp/build/kotlin-webpack/wasmJs/productionExecutable/*.js railway-deploy/
cp composeApp/build/kotlin-webpack/wasmJs/productionExecutable/*.wasm railway-deploy/

echo "✅ Archivos listos para desplegar!"