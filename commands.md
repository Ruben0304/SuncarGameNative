# 📝 Comandos de Referencia Rápida

## 🔨 Build y Desarrollo

```bash
# Build de desarrollo local
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Build de producción
./gradlew :composeApp:wasmJsBrowserDistribution

# Build con verificación
./gradlew verifyBuild

# Limpiar y reconstruir
./gradlew clean :composeApp:wasmJsBrowserDistribution
```

## 🐳 Docker Local

```bash
# Construir imagen
docker build -t solarsurvivor-wasm .

# Ejecutar contenedor
docker run -p 8080:8080 -e PORT=8080 solarsurvivor-wasm

# Con docker-compose
docker-compose up --build

# Logs del contenedor
docker logs solarsurvivor-wasm

# Entrar al contenedor
docker exec -it solarsurvivor-wasm sh

# Limpiar imágenes no usadas
docker system prune -a
```

## 🚂 Railway

```bash
# Login
railway login

# Vincular proyecto
railway link

# Deploy
railway up

# Logs en tiempo real
railway logs -f

# Variables de entorno
railway variables

# Agregar variable
railway variables set KEY=value

# Abrir app en browser
railway open

# Estado del proyecto
railway status

# Ejecutar comando con env vars de Railway
railway run ./gradlew build

# Rollback al deployment anterior
railway down
```

## 🔍 Debugging

```bash
# Ver archivos generados
ls -la composeApp/build/dist/wasmJs/productionExecutable/

# Verificar MIME types en nginx
docker exec solarsurvivor-wasm nginx -T | grep wasm

# Test de health check
curl -I http://localhost:8080

# Ver logs de nginx
docker exec solarsurvivor-wasm tail -f /var/log/nginx/error.log

# Verificar puerto en Railway
railway variables | grep PORT
```

## 📊 Monitoreo

```bash
# Métricas de Docker local
docker stats solarsurvivor-wasm

# Uso de disco
du -sh composeApp/build/dist/wasmJs/productionExecutable/

# Contar archivos
find composeApp/build/dist/wasmJs/productionExecutable/ -type f | wc -l

# Ver tamaño de archivos WASM
ls -lh composeApp/build/dist/wasmJs/productionExecutable/*.wasm
```

## 🧹 Limpieza

```bash
# Limpiar build de Gradle
./gradlew clean

# Limpiar cache de Gradle
./gradlew cleanBuildCache

# Eliminar contenedores Docker
docker-compose down -v

# Eliminar imágenes no usadas
docker image prune -a

# Limpiar todo Docker
docker system prune -a --volumes
```

## 🔧 Troubleshooting

```bash
# Reconstruir sin cache
docker build --no-cache -t solarsurvivor-wasm .

# Gradle con más info
./gradlew :composeApp:wasmJsBrowserDistribution --info

# Gradle con debug
./gradlew :composeApp:wasmJsBrowserDistribution --debug

# Verificar versión de Java
java -version

# Verificar versión de Gradle
./gradlew --version

# Railway debug
RAILWAY_DEBUG=true railway up
```

## 🎯 Scripts Útiles

### Verificar WASM en Browser

```javascript
// Pegar en la consola del browser
fetch('/composeApp.wasm')
  .then(response => {
    console.log('MIME Type:', response.headers.get('content-type'));
    return response.arrayBuffer();
  })
  .then(buffer => {
    console.log('WASM Size:', buffer.byteLength / 1024 / 1024, 'MB');
    return WebAssembly.compile(buffer);
  })
  .then(() => console.log('✅ WASM válido'))
  .catch(err => console.error('❌ Error:', err));
```

### Test de Performance

```bash
# Test de carga simple
for i in {1..100}; do curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080; done | sort | uniq -c
```

## 📌 Aliases Útiles

Agregar a `~/.bashrc` o `~/.zshrc`:

```bash
# Railway shortcuts
alias rw='railway'
alias rwup='railway up'
alias rwlogs='railway logs -f'
alias rwopen='railway open'
alias rwstatus='railway status'

# Gradle shortcuts
alias gw='./gradlew'
alias gwbuild='./gradlew :composeApp:wasmJsBrowserDistribution'
alias gwclean='./gradlew clean'
alias gwrun='./gradlew :composeApp:wasmJsBrowserDevelopmentRun'

# Docker shortcuts
alias dcup='docker-compose up --build'
alias dcdown='docker-compose down -v'
alias dclogs='docker-compose logs -f'
```