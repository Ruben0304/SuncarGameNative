@echo off
echo 🔨 Compilando proyecto WASM...
call gradlew.bat :composeApp:wasmJsBrowserProductionWebpack

if %ERRORLEVEL% neq 0 (
    echo ❌ Error en la compilación
    pause
    exit /b 1
)

echo 🧹 Limpiando directorio de despliegue...
REM Mantener solo Dockerfile y .gitignore
for %%f in (railway-deploy\*) do (
    if not "%%~nxf"=="Dockerfile" (
        if not "%%~nxf"==".gitignore" (
            del /q "%%f"
        )
    )
)
REM Eliminar subdirectorios excepto los necesarios
for /d %%d in (railway-deploy\*) do rmdir /s /q "%%d"

echo 📦 Copiando archivos compilados...

REM Copia archivos HTML y CSS desde processedResources
copy /y "composeApp\build\processedResources\wasmJs\main\*.html" "railway-deploy\"
copy /y "composeApp\build\processedResources\wasmJs\main\*.css" "railway-deploy\"

REM Copia archivos JS y WASM desde kotlin-webpack
copy /y "composeApp\build\kotlin-webpack\wasmJs\productionExecutable\*.js" "railway-deploy\"
copy /y "composeApp\build\kotlin-webpack\wasmJs\productionExecutable\*.wasm" "railway-deploy\"

REM Copia recursos
if exist "composeApp\build\processedResources\wasmJs\main\composeResources" (
    xcopy /e /i /y "composeApp\build\processedResources\wasmJs\main\composeResources" "railway-deploy\composeResources"
)

echo.
echo 📋 Archivos copiados:
dir /b railway-deploy\

echo.
echo ✅ Archivos listos para desplegar!
echo 🚀 Ahora ejecuta: railway up
pause