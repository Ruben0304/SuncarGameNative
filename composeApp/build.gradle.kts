import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    // Android Target
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
            )
        }
    }

    // iOS Targets configurados de manera similar al ejemplo de referencia
    // iOS ARM64 (dispositivos reales)
    iosArm64 {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // iOS Simulator ARM64 (simulador en Mac con Apple Silicon)
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // Exporta las dependencias necesarias para iOS

        }
    }

    // iOS X64 (simulador en Mac con Intel) - Opcional, pero recomendado para compatibilidad
    iosX64 {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

        }
    }

    // WebAssembly JS (si lo necesitas)
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    // JVM Target para Desktop (opcional, si quieres agregar soporte desktop)
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
            )
        }
    }

    // Configuración de Source Sets
    sourceSets {
        // Common Main - Compartido entre todas las plataformas
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Coil para carga de imágenes (opcional)
            implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha06")
        }

        // Common Test
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        // Android Main
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-alpha06")
        }
        

        // iOS Common - Compartido entre todos los targets de iOS
        val iosMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                // Dependencias específicas de iOS si las necesitas
            }
        }

        // iOS ARM64
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        // iOS Simulator ARM64
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        // iOS X64
        val iosX64Main by getting {
            dependsOn(iosMain)
        }

        // WebAssembly Main (si lo estás usando)
        val wasmJsMain by getting {
            dependencies {
                // Dependencias específicas de WASM si las necesitas
            }
        }
    }

    // Configuración global de compilación para todos los targets
    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                // Suprimir warnings específicos si es necesario
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

// Configuración de Android
android {
    namespace = "com.suncar.solarsurvivor"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.suncar.solarsurvivor"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

// Dependencias adicionales para Android
dependencies {
    debugImplementation(compose.uiTooling)
}

// Configuración de Desktop (si agregaste el target desktop)
compose.desktop {
    application {
        mainClass = "com.suncar.solarsurvivor.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.suncar.solarsurvivor"
            packageVersion = "1.0.0"

            macOS {
                bundleID = "com.suncar.solarsurvivor"
            }

            windows {
                upgradeUuid = "YOUR-UNIQUE-UUID-HERE-SOLAR-SURVIVOR"
            }

            linux {
                packageName = "solar-survivor"
                debMaintainer = "your-email@example.com"
            }
        }
    }
}

// Tareas personalizadas útiles

// Tarea para abrir Xcode
tasks.register("openXcode") {
    doLast {
        exec {
            commandLine("open", "iosApp/iosApp.xcodeproj")
        }
    }
}

// Tarea para construir el framework iOS
tasks.register("buildIOSFramework") {
    dependsOn("linkDebugFrameworkIosSimulatorArm64")
    doLast {
        println("iOS Framework built successfully for Solar Survivor")
    }
}

// Tarea para limpiar y reconstruir
tasks.register("cleanBuild") {
    dependsOn("clean", "build")
}

// Tarea para sincronización con iOS
tasks.create("syncIOSApp") {
    dependsOn("linkDebugFrameworkIosSimulatorArm64")
    doLast {
        println("iOS App synchronized with shared framework")
    }
}

// Configuración adicional para todas las tareas de compilación Kotlin
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        )
    }
}