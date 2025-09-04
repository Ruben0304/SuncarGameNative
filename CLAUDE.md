# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**SuncarGameNative** is a Kotlin Multiplatform game called "Solar Survivor" built with Compose Multiplatform. It's a companion educational game for the Suncar solar energy ecosystem, designed to teach users about solar power management through an interactive home energy simulation.

## Key Platforms

- **Android** - Primary target platform for mobile gameplay
- **iOS** - iOS support via Kotlin Multiplatform
- **Web (WASM)** - Web version using Kotlin/WASM for broader accessibility

## Development Commands

### Running the Application

```bash
# Android development
./gradlew :composeApp:assembleDebug           # Build Android APK
./gradlew :composeApp:installDebug            # Install debug APK on connected device
./gradlew :composeApp:installDebugAndroidTest # Install and run tests

# Web/WASM development  
./gradlew :composeApp:wasmJsBrowserDevelopmentRun  # Run web version in browser
./gradlew :composeApp:wasmJsBrowserProductionWebpack  # Build production web bundle

# Testing and verification
./gradlew test                                # Run unit tests
./gradlew build                              # Full build for all platforms
./gradlew clean                              # Clean build artifacts
```

## Architecture Overview

### Game Architecture Pattern

The game follows a **single-activity state machine pattern** with the main game loop in `Game.kt`. Core architectural elements:

- **Central Game State**: All game state is managed in `SolarSurvivorGame()` composable using `remember` and `mutableStateOf`
- **Game Loop**: Time-based simulation using `LaunchedEffect` with configurable speed
- **State Machine**: Uses `GameState` enum to control screen flow and game phases
- **Real-time Updates**: Continuous energy calculations, appliance management, and comfort tracking

### Code Organization

#### Package Structure
```
com.suncar.solarsurvivor/
├── data/              # Game models and enums (Models.kt)
├── ui/
│   ├── components/
│   │   ├── atom/      # Basic UI elements (buttons, items, popups)
│   │   ├── molecules/ # Combined components (metrics, badges, options)  
│   │   └── organisms/ # Complex components (cards, controls, sections)
│   └── screens/       # Game screens (start, game, config, comparison)
└── util/              # Utilities (Icon.kt for custom icons)
```

#### Component Architecture
Follows **Atomic Design** principles:
- **Atoms**: `FlowItem`, `PriorityItem`, `SummaryItem`, `EventPopup`
- **Molecules**: `BudgetOption`, `EnergyIndicator`, `AchievementBadge`
- **Organisms**: `ApplianceCard`, `SpeedControl`, `RecommendationCard`
- **Templates**: Screen layouts in `ui/screens/`

### Core Game Systems

#### Energy Management System
- **Solar Generation**: Time-of-day based calculations with weather simulation
- **Battery Storage**: Charge/discharge mechanics with capacity limits
- **Grid Integration**: Blackout simulation and energy source switching
- **Consumption Tracking**: Real-time appliance power usage monitoring

#### Game Loop Components
Located in `Game.kt:161-334`:
- Time progression (24-hour cycle)
- Blackout schedule processing
- Solar generation calculations
- Comfort level updates
- Energy source management
- Savings/cost calculations

#### State Management
Critical state variables:
- `gameState`: Controls screen flow and game phases
- `timeOfDay`/`currentDay`: Game time progression
- `isBlackout`: Grid power availability
- `energySource`: Current power source (GRID/SOLAR/BATTERY/NONE)
- `appliances`: Map of controllable home devices
- `comfortLevel`/`familyMood`: Player performance metrics

## Key Game Features

### Educational Gameplay Mechanics
1. **Home Energy Simulation**: Realistic appliance power consumption
2. **Solar System Design**: Panel and battery sizing based on consumption
3. **Blackout Survival**: Managing energy during grid outages
4. **Comparison Mode**: Before/after solar installation metrics
5. **Real-time Feedback**: Notifications and recommendations system

### Multi-Platform Considerations
- **Shared Business Logic**: All game mechanics in `commonMain`
- **Platform-Specific UI**: Minimal platform differences (mainly entry points)
- **Resource Management**: Compose Resources for cross-platform assets
- **Performance**: Optimized for mobile with configurable game speed

## Development Guidelines

### Game Logic Patterns
- **State Calculations**: Complex energy calculations in helper functions (`calculateSolarGeneration()`, `calculateTotalConsumption()`)
- **Event Handling**: Centralized appliance toggling with validation (`toggleAppliance()`)  
- **Notification System**: User feedback through `addNotification()` with types and recommendations
- **Time Management**: Coroutine-based game loop with pause/speed controls

### UI Development Patterns
- **Compose State**: Extensive use of `remember` and `mutableStateOf` for reactive UI
- **Material 3 Theming**: Dark theme with solar-inspired color palette
- **Responsive Design**: Grid layouts and adaptive sizing for different screen sizes
- **Animation**: Implicit animations for smooth state transitions

### Testing Strategy
- **Unit Tests**: Focus on energy calculation functions and game state transitions
- **UI Tests**: Screen navigation and user interaction flows
- **Platform Tests**: Ensure multiplatform compatibility

## Technical Dependencies

### Core Technologies
- **Kotlin 2.2.0**: Latest Kotlin with multiplatform support
- **Compose Multiplatform 1.8.2**: UI framework for all platforms  
- **Material 3**: Modern Material Design components
- **Lifecycle ViewModel**: State management with lifecycle awareness

### Build Configuration
- **Gradle Version Catalogs**: Centralized dependency management in `gradle/libs.versions.toml`
- **Target Platforms**: Android API 24+, iOS 13+, Modern browsers (WASM)
- **JVM Target**: Java 11 for Android compatibility

## Application Package Structure

**Android Package**: `com.suncar.solarsurvivor`
**App Name**: Solar Survivor
**Bundle Structure**: 
- Android: Standard APK with Compose UI
- iOS: Framework bundle with native iOS app wrapper
- Web: WASM module with HTML container