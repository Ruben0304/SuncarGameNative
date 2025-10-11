@echo off
REM Solar Survivor - Railway Deployment Script
REM Uses optimized Docker multi-stage build from project root

echo 🚀 Deploying Solar Survivor to Railway...
echo Using multi-stage Dockerfile build strategy

REM Ensure we're in the right directory
if not exist "Dockerfile" (
    echo ❌ Error: Dockerfile not found in current directory
    echo Make sure you're running this from the project root
    pause
    exit /b 1
)

if not exist "railway.json" (
    echo ❌ Error: railway.json not found
    echo Railway configuration is required for deployment
    pause
    exit /b 1
)

echo ✅ Configuration files found
echo 📦 Building and deploying with Railway...

REM Deploy using Railway CLI
where railway >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Using Railway CLI...
    railway up
) else (
    echo ⚠️  Railway CLI not found
    echo Install it with: npm install -g @railway/cli
    echo Or push to your connected GitHub repository for auto-deployment
)

echo 🎮 Solar Survivor deployment initiated!
echo Check your Railway dashboard for build progress
pause