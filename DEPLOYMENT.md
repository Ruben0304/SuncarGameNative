# 🚀 Guía de Despliegue - Kotlin Multiplatform WASM en Railway

## 📋 Pre-requisitos

1. **Railway CLI** instalado: https://docs.railway.app/develop/cli
2. **Docker** instalado localmente (para pruebas)
3. **JDK 17+** y **Gradle 8.4+**
4. Cuenta en [Railway](https://railway.app)

## 🛠️ Configuración Inicial

### 1. Instalar Railway CLI

```bash
# macOS/Linux
curl -fsSL https://railway.app/install.sh | sh

# Windows (PowerShell)
iwr -useb https://railway.app/install.ps1 | iex
```

### 2. Autenticarse en Railway

```bash
railway login
```

### 3. Vincular el Proyecto

```bash
# Si ya tienes un proyecto en Railway
railway link

# Si necesitas crear uno nuevo
railway init
```

## 📦 Estructura de Archivos Necesarios

```
proyecto/
├── Dockerfile
├── nginx.conf.template
├── docker-entrypoint.sh
├── .dockerignore
├── railway.toml
├── railway.json
├── docker-compose.yml (opcional)
├── deploy.sh (opcional)
└── composeApp/
    └── build.gradle.kts
```

## 🔧 Proceso de Despliegue

### Opción 1: Despliegue Automático con Script

```bash
chmod +x deploy.sh
./deploy.sh
```

### Opción 2: Despliegue Manual

#### 1. Construir Localmente (Verificación)

```bash
./gradlew :composeApp:wasmJsBrowserDistribution
```

#### 2. Verificar Archivos Generados

```bash
ls -la composeApp/build/dist/wasmJs/productionExecutable/
```

Deberías ver:
- `index.html`
- `composeApp.js`
- `*.wasm` (archivos WebAssembly)
- Otros recursos estáticos

#### 3. Hacer Ejecutable el Script de Docker

```bash
chmod +x docker-entrypoint.sh
```

#### 4. Desplegar en Railway

```bash
railway up
```

## 🐳 Pruebas Locales con Docker

### Construir Imagen Local

```bash
docker build -t kotlin-wasm-app .
```

### Ejecutar Contenedor Local

```bash
# Opción 1: Docker directo
docker run -p 8080:8080 -e PORT=8080 kotlin-wasm-app

# Opción 2: Docker Compose
docker-compose up
```

Visita: http://localhost:8080

## 🔍 Verificación y Debugging

### Ver Logs en Railway

```bash
railway logs
```

### Abrir la Aplicación

```bash
railway open
```

### Variables de Entorno en Railway

Railway automáticamente proporciona:
- `PORT`: Puerto dinámico asignado
- `RAILWAY_ENVIRONMENT`: Nombre del ambiente
- `RAILWAY_PROJECT_ID`: ID del proyecto

## ⚠️ Problemas Comunes y Soluciones

### 1. Error: "wasm streaming compile failed"

**Causa:** MIME type incorrecto para archivos WASM
**Solución:** Verifica que `nginx.conf.template` incluya:
```nginx
types {
    application/wasm wasm;
}
```

### 2. Error: "404 en rutas SPA"

**Causa:** nginx no está configurado para SPA
**Solución:** Asegúrate que nginx.conf incluya:
```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

### 3. Error: "Port binding failed"

**Causa:** No se está usando la variable PORT de Railway
**Solución:** Verifica que docker-entrypoint.sh use `$PORT`

### 4. Build muy lento o timeout

**Causa:** Tiempo de build insuficiente
**Solución:** En `railway.toml`:
```toml
[build]
buildTimeout = 1800  # 30 minutos
```

### 5. Archivos WASM muy grandes

**Optimizaciones:**
- Habilita minificación en Gradle
- Usa compresión gzip en nginx
- Considera lazy loading de módulos

## 🎯 Optimizaciones de Rendimiento

### 1. Cache de Browser

El nginx.conf ya incluye:
- Cache de 30 días para assets estáticos
- Cache de 1 hora para archivos WASM

### 2. Compresión

Gzip está habilitado para:
- JavaScript, CSS, HTML
- Archivos WASM
- Fuentes web

### 3. Headers de Seguridad

Incluidos:
- X-Frame-Options
- X-Content-Type-Options
- X-XSS-Protection

## 📊 Monitoreo

### Métricas en Railway

1. Ve a tu proyecto en Railway Dashboard
2. Click en el servicio
3. Pestaña "Metrics" para ver:
    - Uso de CPU
    - Memoria
    - Red
    - Logs en tiempo real

### Health Check

Railway hace health checks automáticos en `/`
Configurado en `railway.toml`:
```toml
healthcheckPath = "/"
healthcheckTimeout = 120
```

## 🔄 Actualizaciones y Redeploys

### Deploy Automático (GitHub)

1. Conecta tu repo de GitHub en Railway
2. Cada push a `main` dispara un deploy automático

### Deploy Manual

```bash
railway up
```

### Rollback

En Railway Dashboard:
1. Ve a "Deployments"
2. Encuentra el deployment anterior
3. Click en "Rollback"

## 📝 Checklist Final

- [ ] Dockerfile creado y configurado
- [ ] nginx.conf.template con MIME types WASM
- [ ] docker-entrypoint.sh ejecutable
- [ ] railway.toml configurado
- [ ] Variables de entorno configuradas en Railway
- [ ] Build local exitoso
- [ ] Prueba local con Docker funcionando
- [ ] Deploy en Railway exitoso
- [ ] Health checks pasando
- [ ] Aplicación accesible públicamente

## 🆘 Soporte

- [Railway Docs](https://docs.railway.app)
- [Kotlin/WASM Docs](https://kotlinlang.org/docs/wasm-overview.html)
- [Railway Discord](https://discord.gg/railway)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/railway)

## 🎉 ¡Listo!

Tu aplicación Kotlin Multiplatform WASM ahora está desplegada en Railway con:
- ✅ Servidor nginx optimizado
- ✅ Soporte completo para WebAssembly
- ✅ Configuración SPA
- ✅ HTTPS automático
- ✅ Auto-scaling
- ✅ CI/CD integrado