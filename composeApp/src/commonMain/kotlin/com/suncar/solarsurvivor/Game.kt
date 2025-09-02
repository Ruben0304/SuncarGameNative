package com.suncar.suncargame

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.*
import com.suncar.solarsurvivor.ui.screens.*
import com.suncar.solarsurvivor.ui.components.*
import com.suncar.solarsurvivor.ui.components.atom.EventPopup
import com.suncar.solarsurvivor.ui.components.molecules.AchievementBadge
import com.suncar.solarsurvivor.ui.components.organisms.NotificationCard
import kotlin.random.Random
import kotlinx.coroutines.delay

@Composable
fun SolarSurvivorGame() {
        var gameState by remember { mutableStateOf(GameState.START) }
        var currentDay by remember { mutableStateOf(1) }
        var timeOfDay by remember { mutableStateOf(6) }
        var isBlackout by remember { mutableStateOf(false) }
        var blackoutSchedule by remember { mutableStateOf<List<BlackoutSchedule>>(emptyList()) }
        var gameSpeed by remember { mutableStateOf(3000L) }
        var dayStarted by remember { mutableStateOf(false) }

        // Sistema de energía
        var energySource by remember { mutableStateOf(EnergySource.GRID) }
        var solarPanels by remember { mutableStateOf(0) }
        var batteries by remember { mutableStateOf(0) }
        var currentSolarGeneration by remember { mutableStateOf(0) }
        var batteryCharge by remember { mutableStateOf(0f) }
        var maxBatteryCapacity by remember { mutableStateOf(0f) }

        // Métricas del hogar
        var comfortLevel by remember { mutableStateOf(100f) }
        var familyMood by remember { mutableStateOf("normal") }
        var moneySpent by remember { mutableStateOf(0f) }
        var moneySaved by remember { mutableStateOf(0f) }
        var co2Saved by remember { mutableStateOf(0f) }
        var foodSpoilage by remember { mutableStateOf(0f) }
        var productivity by remember { mutableStateOf(100f) }
        var hygiene by remember { mutableStateOf(100f) }
        var temperature by remember { mutableStateOf(28f) }

        // Electrodomésticos
        var appliances by remember {
                mutableStateOf(
                        mapOf(
                                "refrigerator" to
                                        Appliance("Refrigerador", Icons.Default.Kitchen, Color(0xFF4FC3F7), false, 1, 150, true),
                                "lights" to Appliance("Iluminación", Icons.Default.Lightbulb, Color(0xFFFFD700), false, 2, 60),
                                "fan" to Appliance("Ventilador", Icons.Default.Air, Color(0xFF81C784), false, 3, 75),
                                "tv" to Appliance("Televisor", Icons.Default.Tv, Color(0xFF9C27B0), false, 4, 100),
                                "ac" to Appliance("Aire Acondicionado", Icons.Default.AcUnit, Color(0xFF29B6F6), false, 5, 1500),
                                "waterHeater" to Appliance("Calentador", Icons.Default.Whatshot, Color(0xFFFF5722), false, 6, 1200),
                                "computer" to Appliance("Computadora", Icons.Default.Computer, Color(0xFF757575), false, 7, 300),
                                "phone" to Appliance("Cargador Móvil", Icons.Default.PhoneAndroid, Color(0xFF4CAF50), false, 8, 10),
                                "washingMachine" to Appliance("Lavadora", Icons.Default.LocalLaundryService, Color(0xFF03A9F4), false, 9, 500),
                                "microwave" to Appliance("Microondas", Icons.Default.Microwave, Color(0xFFFF9800), false, 10, 800)
                        )
                )
        }

        // Eventos y notificaciones
        var currentEvent by remember { mutableStateOf<Event?>(null) }
        var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
        var achievements by remember { mutableStateOf<List<Achievement>>(emptyList()) }
        var score by remember { mutableStateOf(0) }

        // Configuración y estadísticas
        var selectedHome by remember { mutableStateOf("medium") }
        var dayStats by remember { mutableStateOf(DayStats()) }
        var comparison by remember {
                mutableStateOf(
                        mapOf(
                                "withoutSolar" to null as ComparisonData?,
                                "withSolar" to null as ComparisonData?
                        )
                )
        }

        // Handle notification removal
        LaunchedEffect(notifications.size) {
                if (notifications.isNotEmpty()) {
                        delay(1000)
                        notifications = notifications.drop(1)
                }
        }

        // Helper Functions
        fun calculateSolarGeneration(): Int {
                if (solarPanels == 0) return 0

                val peakPower = solarPanels * 505
                val weather = if (Random.nextFloat() > 0.8f) 0.7f else 1f

                val efficiency =
                        when {
                                timeOfDay in 6..7 -> 0.15f * weather
                                timeOfDay in 8..9 -> 0.4f * weather
                                timeOfDay in 10..13 -> 0.85f * weather
                                timeOfDay in 14..16 -> 0.6f * weather
                                timeOfDay in 17..18 -> 0.25f * weather
                                else -> 0f
                        }

                return (peakPower * efficiency).toInt()
        }

        fun calculateTotalConsumption(): Int {
                return appliances.values.sumOf { if (it.on) it.consumption else 0 }
        }

        fun addNotification(
                message: String,
                type: NotificationType = NotificationType.INFO,
                recommendation: String? = null
        ) {
                val id = kotlin.random.Random.nextLong()
                notifications = notifications + Notification(id, message, type, recommendation)
        }

        fun toggleAppliance(appKey: String) {
                if (gameState != GameState.PLAYING) return

                val consumption = calculateTotalConsumption()
                val available =
                        currentSolarGeneration +
                                (if (isBlackout) 0 else 2000) +
                                (if (batteryCharge > 0) 1000 else 0)

                val app = appliances[appKey] ?: return

                if (!app.on && consumption + app.consumption > available) {
                        addNotification(
                                "No hay suficiente energía para ${app.name}",
                                NotificationType.ERROR,
                                "Apaga otros dispositivos o instala más paneles solares"
                        )
                        return
                }

                if (app.on && app.essential) {
                        addNotification(
                                "${app.name} apagado - dispositivo esencial",
                                NotificationType.WARNING,
                                "Este dispositivo es importante para el confort familiar"
                        )
                }

                appliances =
                        appliances.toMutableMap().apply { this[appKey] = app.copy(on = !app.on) }
        }

        // Game Loop Effect
        LaunchedEffect(gameState, dayStarted) {
                if (gameState == GameState.PLAYING && dayStarted) {
                        while (true) {
                                delay(gameSpeed)

                                // Update time
                                timeOfDay = (timeOfDay + 1) % 24

                                if (timeOfDay == 0) {
                                        dayStarted = false
                                        currentDay++
                                        gameState = GameState.BLACKOUT_CONFIG
                                        break
                                }

                                // Check blackouts
                                val inBlackout =
                                        blackoutSchedule.any {
                                                timeOfDay >= it.start && timeOfDay < it.end
                                        }

                                if (inBlackout && !isBlackout) {
                                        isBlackout = true
                                        val nextSchedule =
                                                blackoutSchedule.find {
                                                        timeOfDay >= it.start && timeOfDay < it.end
                                                }
                                        val duration = nextSchedule?.let { it.end - it.start } ?: 4
                                        addNotification(
                                                "APAGÓN - Sin electricidad por $duration horas",
                                                NotificationType.ERROR,
                                                if (solarPanels > 0)
                                                        "Tu sistema solar mantendrá la energía disponible"
                                                else
                                                        "Prioriza dispositivos esenciales como el refrigerador"
                                        )
                                } else if (!inBlackout && isBlackout) {
                                        isBlackout = false
                                        addNotification(
                                                "✅ Regresó la electricidad",
                                                NotificationType.SUCCESS,
                                                "Enciende dispositivos prioritarios primero"
                                        )
                                }

                                // Update solar generation
                                currentSolarGeneration = calculateSolarGeneration()

                                // Update comfort level
                                comfortLevel =
                                        maxOf(
                                                0f,
                                                minOf(
                                                        100f,
                                                        comfortLevel +
                                                                when {
                                                                        !appliances[
                                                                                        "refrigerator"]!!
                                                                                .on -> {
                                                                                foodSpoilage =
                                                                                        minOf(
                                                                                                100f,
                                                                                                foodSpoilage +
                                                                                                        5f
                                                                                        )
                                                                                -3f
                                                                        }
                                                                        else -> {
                                                                                foodSpoilage =
                                                                                        maxOf(
                                                                                                0f,
                                                                                                foodSpoilage -
                                                                                                        2f
                                                                                        )
                                                                                0f
                                                                        }
                                                                } +
                                                                when {
                                                                        !appliances["lights"]!!
                                                                                .on &&
                                                                                (timeOfDay < 6 ||
                                                                                        timeOfDay >
                                                                                                18) -> {
                                                                                productivity =
                                                                                        maxOf(
                                                                                                0f,
                                                                                                productivity -
                                                                                                        3f
                                                                                        )
                                                                                -2f
                                                                        }
                                                                        else -> 0f
                                                                } +
                                                                when {
                                                                        energySource ==
                                                                                EnergySource.NONE ->
                                                                                -5f
                                                                        isBlackout &&
                                                                                (energySource ==
                                                                                        EnergySource
                                                                                                .SOLAR ||
                                                                                        energySource ==
                                                                                                EnergySource
                                                                                                        .BATTERY) ->
                                                                                2f
                                                                        else -> 0f
                                                                }
                                                )
                                        )

                                // Update family mood
                                familyMood =
                                        when {
                                                comfortLevel < 30 -> "angry"
                                                comfortLevel < 50 -> "frustrated"
                                                comfortLevel < 70 -> "uncomfortable"
                                                comfortLevel < 85 -> "normal"
                                                else -> "happy"
                                        }

                                // Update energy source
                                val consumption = calculateTotalConsumption()
                                val solarGen = calculateSolarGeneration()
                                currentSolarGeneration = solarGen

                                energySource =
                                        when {
                                                isBlackout ->
                                                        when {
                                                                solarGen >= consumption -> {
                                                                        if (batteries > 0) {
                                                                                val excess =
                                                                                        solarGen -
                                                                                                consumption
                                                                                batteryCharge =
                                                                                        minOf(
                                                                                                batteryCharge +
                                                                                                        (excess /
                                                                                                                1000f) *
                                                                                                                batteries *
                                                                                                                100,
                                                                                                maxBatteryCapacity
                                                                                        )
                                                                        }
                                                                        EnergySource.SOLAR
                                                                }
                                                                batteryCharge > 10 -> {
                                                                        batteryCharge =
                                                                                maxOf(
                                                                                        0f,
                                                                                        batteryCharge -
                                                                                                (consumption /
                                                                                                        1000f) *
                                                                                                        1000
                                                                                )
                                                                        EnergySource.BATTERY
                                                                }
                                                                else -> EnergySource.NONE
                                                        }
                                                else -> EnergySource.GRID
                                        }

                                // Update savings
                                if (solarGen > 0) {
                                        moneySaved += solarGen * 0.00015f
                                        co2Saved += solarGen * 0.0004f
                                }

                                if (energySource == EnergySource.GRID && solarPanels == 0) {
                                        moneySpent += consumption * 0.00015f
                                }
                        }
                }
        }

        // Main UI
        MaterialTheme(
                colorScheme =
                        darkColorScheme(
                                primary = Color(0xFFFFD700),
                                secondary = Color(0xFF764BA2),
                                background = Color(0xFF1A1A2E),
                                surface = Color(0xFF16213E)
                        )
        ) {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) {
                        Box {
                                when (gameState) {
                                        GameState.START ->
                                                StartScreen(
                                                        onStart = { mode ->
                                                                if (mode == "withSolar") {
                                                                        gameState =
                                                                                GameState
                                                                                        .SOLAR_STUDY
                                                                } else {
                                                                        gameState =
                                                                                GameState
                                                                                        .BLACKOUT_CONFIG
                                                                        currentDay = 1
                                                                        solarPanels = 0
                                                                        batteries = 0
                                                                }
                                                                timeOfDay = 6
                                                                comfortLevel = 100f
                                                                foodSpoilage = 0f
                                                                productivity = 100f
                                                                hygiene = 100f
                                                                temperature = 28f
                                                                appliances =
                                                                        appliances.mapValues {
                                                                                it.value.copy(
                                                                                        on = false
                                                                                )
                                                                        }
                                                        }
                                                )
                                        GameState.BLACKOUT_CONFIG ->
                                                BlackoutConfigScreen(
                                                        day = currentDay,
                                                        onConfirm = { schedule ->
                                                                blackoutSchedule = schedule
                                                                dayStarted = true
                                                                gameState = GameState.PLAYING

                                                                val totalHours =
                                                                        schedule.sumOf {
                                                                                it.end - it.start
                                                                        }
                                                                addNotification(
                                                                        "📅 Día $currentDay - ${totalHours}h de apagones programados",
                                                                        NotificationType.INFO,
                                                                        if (currentDay == 1)
                                                                                "Gestiona tu hogar sin energía solar. Será difícil."
                                                                        else
                                                                                "Con tu sistema solar, los apagones no te afectarán"
                                                                )
                                                        }
                                                )
                                        GameState.SOLAR_STUDY ->
                                                SolarStudyScreen(
                                                        appliances = appliances,
                                                        onComplete = { panels, batts ->
                                                                solarPanels = panels
                                                                batteries = batts
                                                                maxBatteryCapacity = batts * 5000f
                                                                batteryCharge = batts * 2500f

                                                                if (currentDay == 1) {
                                                                        comparison =
                                                                                comparison +
                                                                                        ("withoutSolar" to
                                                                                                ComparisonData(
                                                                                                        comfort =
                                                                                                                comfortLevel,
                                                                                                        moneySpent =
                                                                                                                moneySpent,
                                                                                                        blackoutsSurvived =
                                                                                                                dayStats.blackoutTime,
                                                                                                        foodLost =
                                                                                                                foodSpoilage,
                                                                                                        productivity =
                                                                                                                productivity
                                                                                                ))
                                                                }

                                                                currentDay = 2
                                                                gameState =
                                                                        GameState.BLACKOUT_CONFIG
                                                                timeOfDay = 6
                                                                comfortLevel = 100f
                                                                moneySpent = 0f
                                                                foodSpoilage = 0f
                                                                productivity = 100f
                                                                hygiene = 100f

                                                                addNotification(
                                                                        "Sistema instalado: $panels paneles, $batts baterías",
                                                                        NotificationType.SUCCESS,
                                                                        "Capacidad: ${panels * 505}W pico, ${batts * 5}kWh almacenamiento"
                                                                )
                                                        }
                                                )
                                        GameState.CONFIGURING ->
                                                ConfigurationScreen(
                                                        onApply = { panels, batts ->
                                                                solarPanels = panels
                                                                batteries = batts
                                                                maxBatteryCapacity = batts * 5000f
                                                                batteryCharge = batts * 2500f

                                                                if (currentDay == 1) {
                                                                        comparison =
                                                                                comparison +
                                                                                        ("withoutSolar" to
                                                                                                ComparisonData(
                                                                                                        comfort =
                                                                                                                comfortLevel,
                                                                                                        moneySpent =
                                                                                                                moneySpent,
                                                                                                        blackoutsSurvived =
                                                                                                                dayStats.blackoutTime,
                                                                                                        foodLost =
                                                                                                                foodSpoilage,
                                                                                                        productivity =
                                                                                                                productivity
                                                                                                ))
                                                                }

                                                                currentDay = 2
                                                                gameState =
                                                                        GameState.BLACKOUT_CONFIG
                                                                timeOfDay = 6
                                                                comfortLevel = 100f
                                                                moneySpent = 0f
                                                                foodSpoilage = 0f
                                                                productivity = 100f
                                                                hygiene = 100f
                                                        }
                                                )
                                        GameState.COMPARISON ->
                                                ComparisonScreen(
                                                        comparison = comparison,
                                                        score = score,
                                                        onShare = {
                                                                // Share functionality
                                                        },
                                                        onQuote = {
                                                                // Quote request functionality
                                                        }
                                                )
                                        GameState.PLAYING ->
                                                GameScreen(
                                                        currentDay = currentDay,
                                                        timeOfDay = timeOfDay,
                                                        isBlackout = isBlackout,
                                                        energySource = energySource,
                                                        solarPanels = solarPanels,
                                                        batteries = batteries,
                                                        currentSolarGeneration =
                                                                currentSolarGeneration,
                                                        batteryCharge = batteryCharge,
                                                        maxBatteryCapacity = maxBatteryCapacity,
                                                        comfortLevel = comfortLevel,
                                                        appliances = appliances,
                                                        foodSpoilage = foodSpoilage,
                                                        productivity = productivity,
                                                        hygiene = hygiene,
                                                        temperature = temperature,
                                                        moneySaved = moneySaved,
                                                        co2Saved = co2Saved,
                                                        score = score,
                                                        gameSpeed = gameSpeed,
                                                        onSpeedChange = { gameSpeed = it },
                                                        onToggleAppliance = { toggleAppliance(it) },
                                                        onConfigureClick = {
                                                                if (currentDay == 1) {
                                                                        gameState =
                                                                                GameState
                                                                                        .CONFIGURING
                                                                } else {
                                                                        comparison =
                                                                                comparison +
                                                                                        ("withSolar" to
                                                                                                ComparisonData(
                                                                                                        comfort =
                                                                                                                comfortLevel,
                                                                                                        moneySaved =
                                                                                                                moneySaved,
                                                                                                        co2Saved =
                                                                                                                co2Saved
                                                                                                ))
                                                                        gameState =
                                                                                GameState.COMPARISON
                                                                }
                                                        }
                                                )
                                        else -> {}
                                }

                                // Notifications overlay
                                Column(
                                        modifier =
                                                Modifier.align(Alignment.BottomStart)
                                                        .padding(16.dp)
                                                        .widthIn(max = 400.dp)
                                ) {
                                        notifications.forEach { notification ->
                                                NotificationCard(notification)
                                                Spacer(modifier = Modifier.height(8.dp))
                                        }
                                }

                                // Events overlay
                                currentEvent?.let { event -> EventPopup(event) }

                                // Achievements overlay
                                Column(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                                        achievements.forEach { achievement ->
                                                AchievementBadge(achievement)
                                                Spacer(modifier = Modifier.height(8.dp))
                                        }
                                }
                        }
                }
        }
}


