# Solar Survivor - Railway Deployment Guide

## 🎯 Optimized Deployment Strategy (2025)

This project uses a **Docker multi-stage build** strategy, following Railway's best practices for Kotlin Multiplatform WASM applications.

## 🏗️ Architecture

### Multi-Stage Docker Build
```dockerfile
Stage 1 (Builder): gradle:8.5-jdk17
- Builds WASM production bundle with Gradle
- Runs wasmJsBrowserProductionWebpack
- Generates optimized artifacts

Stage 2 (Server): nginx:alpine
- Lightweight nginx server
- Proper CORS headers for WASM
- Railway PORT configuration
- Gzip compression enabled
```

## 🚀 Deployment Methods

### Method 1: Automatic GitHub Deployment (Recommended)
1. Push changes to your connected GitHub repository
2. Railway automatically detects `Dockerfile` and `railway.json`
3. Multi-stage build runs automatically
4. App deploys with zero downtime

### Method 2: Railway CLI
```bash
# Install Railway CLI (if not installed)
npm install -g @railway/cli

# Deploy from project root
railway login
railway up
```

### Method 3: Quick Deploy Scripts
```bash
# Linux/Mac
./deploy.sh

# Windows
deploy.bat
```

## 📁 Project Structure

```
SuncarGameNative/
├── Dockerfile              # Multi-stage build configuration
├── railway.json           # Railway deployment config
├── .dockerignore         # Optimizes build context
├── deploy.sh/.bat        # Deployment scripts
└── composeApp/           # Source code
    └── build/dist/wasmJs/productionExecutable/  # Build output
```

## ⚙️ Configuration Files

### railway.json
- Forces Dockerfile builder (overrides Nixpacks)
- Configures health checks
- Sets restart policies

### .dockerignore
- Excludes unnecessary files from build context
- Improves build performance
- Reduces image size

## 🔧 Build Process

1. **Gradle Build**: Compiles Kotlin/Multiplatform to WASM
2. **Production Webpack**: Bundles for production with optimization
3. **nginx Setup**: Configures web server with WASM support
4. **Railway Deploy**: Automatic deployment with health checks

## 🌐 WASM Configuration

The nginx configuration includes:
- **CORS Headers**: Required for WASM execution
- **MIME Types**: Proper `application/wasm` content type
- **Gzip Compression**: Reduces bundle size
- **Caching**: Optimizes static asset delivery

## 🗂️ Legacy Files (Can be Removed)

The `railway-deploy/` folder contains legacy manual deployment files:
- Manual asset copying approach
- Multiple deployment scripts
- Static file copies

**These are no longer needed** with the new Docker strategy.

## 🎮 Benefits of New Strategy

1. **🔄 Automated**: No manual file copying
2. **🚀 Faster**: Multi-stage builds with caching
3. **🛡️ Reliable**: Consistent build environment
4. **📦 Optimized**: Smaller images, better compression
5. **🔧 Maintainable**: Single source of truth in Dockerfile

## 🧪 Testing

Test the build locally:
```bash
# Build Docker image
docker build -t solar-survivor .

# Run locally
docker run -p 8080:8080 solar-survivor
```

Visit `http://localhost:8080` to verify the game works correctly.

## 🐛 Troubleshooting

### Build Fails
- Check Gradle version compatibility
- Verify all source files are included in Docker context
- Check .dockerignore for excluded files

### WASM Loading Issues
- Verify CORS headers are set correctly
- Check browser developer console for errors
- Ensure proper MIME types for .wasm files

### Railway Deployment Issues
- Verify railway.json is in project root
- Check Railway dashboard for build logs
- Ensure GitHub repository is connected properly