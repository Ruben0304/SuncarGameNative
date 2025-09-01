# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**SuncarGameNative** is a Kotlin Multiplatform Compose project that implements "Solar Survivor" - a solar energy management game. The game appears to simulate managing solar energy systems, appliances, and power consumption with educational and entertainment value related to solar energy concepts.

## Platform Support

This is a multiplatform project targeting:
- **Android** - Primary mobile platform
- **iOS** - iOS mobile platform  
- **Web (Wasm)** - Web browser deployment
- **Desktop** - JVM-based desktop application

## Development Commands

### Android Development
```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Install debug APK on connected device/emulator
./gradlew :composeApp:installDebug

# Run on Android
./gradlew :composeApp:run
```

### iOS Development
```bash
# Open iOS project in Xcode (macOS only)
open iosApp/iosApp.xcodeproj

# Build iOS framework
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

### Web Development
```bash
# Run web development server
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Build web production bundle
./gradlew :composeApp:wasmJsBrowserDistribution
```

### Testing
```bash
# Run all tests
./gradlew test

# Run common tests
./gradlew :composeApp:testDebugUnitTest

# Run Android tests specifically
./gradlew :composeApp:connectedAndroidTest
```

### General Build Commands
```bash
# Clean build
./gradlew clean

# Full build all platforms
./gradlew build

# Check project dependencies
./gradlew dependencies
```

## Architecture Overview

### Multiplatform Structure
The project follows Kotlin Multiplatform conventions:

- **`composeApp/src/commonMain/`** - Shared business logic, UI components, and game mechanics
- **`composeApp/src/androidMain/`** - Android-specific implementations and platform integrations
- **`composeApp/src/iosMain/`** - iOS-specific implementations  
- **`composeApp/src/wasmJsMain/`** - Web/browser-specific implementations
- **`iosApp/`** - iOS application shell and SwiftUI integration

### Key Technologies
- **Kotlin Multiplatform** - Cross-platform development
- **Compose Multiplatform** - Declarative UI framework across all platforms
- **Material 3** - Modern Material Design components
- **Kotlin Coroutines** - Asynchronous programming for game mechanics

### Game Architecture
Based on the codebase analysis, the game implements:

- **Solar Energy Management** - Players manage solar power systems
- **Appliance Control** - Turn devices on/off based on available power
- **Priority Management** - Essential vs. non-essential device prioritization  
- **Blackout Simulation** - Time-based power availability challenges
- **Notification System** - Game events and status updates
- **Real-time Updates** - Dynamic power consumption calculations

### Package Structure
```
com.suncar.solarsurvivor/
├── Game.kt              # Main game logic and UI components
├── App.kt               # Application entry point
├── Greeting.kt          # Basic app components
├── Platform.kt          # Platform-specific implementations
└── MainActivity.kt      # Android activity (androidMain)
```

## Game Mechanics

The Solar Survivor game includes several core data models:

- **Appliances** - Devices with power consumption, priority levels, and states
- **BlackoutSchedule** - Time-based power availability periods
- **Notifications** - In-game messaging and alerts
- **Power Management** - Real-time energy balance calculations

### Key Game Features
- Interactive appliance control with priority management
- Dynamic power consumption simulation
- Educational solar energy concepts
- Cross-platform consistent gameplay experience
- Material Design 3 UI with solar energy theming

## Platform-Specific Notes

### Android Configuration
- **Package**: `com.suncar.solarsurvivor`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 14)
- **Compile SDK**: 35

### iOS Integration
- Uses SwiftUI for iOS-specific UI elements
- Integrates with Compose Multiplatform shared UI
- Requires Xcode for iOS development and deployment

### Web Deployment
- Uses Kotlin/Wasm for web compilation
- Webpack configuration for development server
- Static asset serving for web resources

## Dependencies Management

The project uses Gradle version catalogs (`gradle/libs.versions.toml`):
- **Kotlin**: 2.2.0
- **Compose Multiplatform**: 1.8.2  
- **AGP**: 8.10.0
- **AndroidX Lifecycle**: 2.9.1
- **Material Icons Extended**: 1.6.1

## Development Workflow

1. **Local Development**: Use `./gradlew :composeApp:run` for quick iterations
2. **Android Testing**: Use Android Studio or `./gradlew :composeApp:installDebug`
3. **Web Development**: Use `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
4. **iOS Development**: Requires macOS and Xcode for full iOS testing

## Code Conventions

- Follow Kotlin coding conventions
- Use Compose best practices for UI development
- Implement platform-specific features in respective platform directories
- Keep shared business logic in `commonMain`
- Use Material 3 design system consistently across platforms