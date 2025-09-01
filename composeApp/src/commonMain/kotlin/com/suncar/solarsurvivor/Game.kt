package com.suncar.suncargame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.*
import kotlin.random.Random
import kotlinx.coroutines.delay

// Icon Utility
@Composable
fun IconWithColor(
    imageVector: ImageVector,
    tint: Color = Color.Unspecified,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier.size(size),
        tint = tint
    )
}

// Data Classes
data class Appliance(
        val name: String,
        val icon: ImageVector,
        val iconColor: Color = Color.Unspecified,
        var on: Boolean = false,
        val priority: Int,
        val consumption: Int,
        val essential: Boolean = false,
        var autoOn: Boolean = false
)

data class BlackoutSchedule(val start: Int, val end: Int)

data class Notification(
        val id: Long,
        val message: String,
        val type: NotificationType,
        val recommendation: String? = null
)

enum class NotificationType {
        SUCCESS,
        WARNING,
        ERROR,
        INFO
}

data class Achievement(
        val id: String,
        val title: String,
        val description: String,
        val icon: ImageVector
)

data class Event(
        val id: String,
        val title: String,
        val description: String,
        val effect: () -> Unit
)

data class ComparisonData(
        val comfort: Float,
        val moneySpent: Float? = null,
        val moneySaved: Float? = null,
        val co2Saved: Float? = null,
        val blackoutsSurvived: Int? = null,
        val foodLost: Float? = null,
        val productivity: Float? = null
)

data class DayStats(
        val blackoutTime: Int = 0,
        val comfortLoss: Int = 0,
        val energyFromSolar: Float = 0f,
        val energyFromGrid: Float = 0f,
        val energyFromBattery: Float = 0f
)

enum class GameState {
        START,
        TUTORIAL,
        PLAYING,
        CONFIGURING,
        COMPARISON,
        BLACKOUT_CONFIG,
        SOLAR_STUDY
}

enum class EnergySource {
        GRID,
        SOLAR,
        BATTERY,
        NONE
}

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
                        delay(10000)
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

@Composable
fun StartScreen(onStart: (String) -> Unit) {
        Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
                // Hero Section
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                ) {
                        Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sun",
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                                text = "Solar Survivor",
                                style =
                                        MaterialTheme.typography.displayLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 48.sp
                                        ),
                                color = Color(0xFFFFD700)
                        )
                }

                Text(
                        text = "Sobrevive los apagones cubanos con energía solar",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 16.dp, bottom = 48.dp)
                )

                // Start Options
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                                Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
                ) {
                        // Without Solar Option
                        Card(
                                modifier =
                                        Modifier.weight(1f).clickable { onStart("withoutSolar") },
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0xFF3E3E52)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                                Column(
                                        modifier = Modifier.padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = "Warning",
                                                modifier = Modifier.size(48.dp),
                                                tint = Color(0xFFFF6B6B)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                                text = "Día Sin Paneles Solares",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                                text =
                                                        "Experimenta la dura realidad de los apagones diarios cubanos",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White.copy(alpha = 0.8f),
                                                textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                                onClick = { onStart("withoutSolar") },
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFF667EEA)
                                                        )
                                        ) {
                                                Icon(
                                                        Icons.Default.PlayArrow,
                                                        contentDescription = null
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Empezar Sufrimiento")
                                        }
                                }
                        }

                        // With Solar Option
                        Card(
                                modifier = Modifier.weight(1f).clickable { onStart("withSolar") },
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0xFF3A5F3A)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                border = BorderStroke(2.dp, Color(0xFFFFD700))
                        ) {
                                Column(
                                        modifier = Modifier.padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.WbSunny,
                                                contentDescription = "Sun",
                                                modifier = Modifier.size(48.dp),
                                                tint = Color(0xFFFFD700)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                                text = "Día Con Sistema Solar",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                                text =
                                                        "Descubre la libertad de la independencia energética",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White.copy(alpha = 0.8f),
                                                textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                                onClick = { onStart("withSolar") },
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFF4CAF50)
                                                        )
                                        ) {
                                                Icon(Icons.Default.Bolt, contentDescription = null)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Experimentar Libertad")
                                        }
                                }
                        }
                }

                // Game Info
                Spacer(modifier = Modifier.height(48.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                        InfoItem(Icons.Default.Schedule, "15-20 min de juego")
                        InfoItem(Icons.Default.EmojiEvents, "Desbloquea logros")
                        InfoItem(Icons.Default.TrendingUp, "Datos reales de Cuba")
                }
        }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                )
        }
}

@Composable
fun BlackoutConfigScreen(day: Int, onConfirm: (List<BlackoutSchedule>) -> Unit) {
        var schedules by remember { mutableStateOf(listOf(BlackoutSchedule(10, 14))) }

        val scrollState = rememberScrollState()

        Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                                text = "Programa de Apagones - Día $day",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                        )
                }

                Text(
                        text = "Configura los horarios de cortes eléctricos para hoy",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                // Quick suggestions
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                                Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                        OutlinedButton(
                                onClick = {
                                        schedules =
                                                listOf(
                                                        BlackoutSchedule(10, 14),
                                                        BlackoutSchedule(18, 22)
                                                )
                                },
                                colors =
                                        ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color.White
                                        ),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                        ) { Text("La Habana (8h)") }
                        OutlinedButton(
                                onClick = {
                                        schedules =
                                                listOf(
                                                        BlackoutSchedule(8, 13),
                                                        BlackoutSchedule(14, 18),
                                                        BlackoutSchedule(19, 23)
                                                )
                                },
                                colors =
                                        ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color.White
                                        ),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                        ) { Text("Provincias (12h)") }
                        OutlinedButton(
                                onClick = {
                                        schedules =
                                                listOf(
                                                        BlackoutSchedule(6, 11),
                                                        BlackoutSchedule(12, 17),
                                                        BlackoutSchedule(18, 22),
                                                        BlackoutSchedule(23, 23)
                                                )
                                },
                                colors =
                                        ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color(0xFFFF6B6B)
                                        ),
                                border = BorderStroke(1.dp, Color(0xFFFF6B6B))
                        ) { Text("Zona Crítica (18h)") }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Schedules
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
                ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                                schedules.forEachIndexed { index, schedule ->
                                        ScheduleRow(
                                                index = index + 1,
                                                schedule = schedule,
                                                onStartChange = { newStart ->
                                                        schedules =
                                                                schedules.toMutableList().apply {
                                                                        this[index] =
                                                                                schedule.copy(
                                                                                        start =
                                                                                                newStart
                                                                                )
                                                                }
                                                },
                                                onEndChange = { newEnd ->
                                                        schedules =
                                                                schedules.toMutableList().apply {
                                                                        this[index] =
                                                                                schedule.copy(
                                                                                        end = newEnd
                                                                                )
                                                                }
                                                },
                                                onRemove =
                                                        if (schedules.size > 1) {
                                                                {
                                                                        schedules =
                                                                                schedules
                                                                                        .filterIndexed {
                                                                                                i,
                                                                                                _ ->
                                                                                                i !=
                                                                                                        index
                                                                                        }
                                                                }
                                                        } else null
                                        )
                                        if (index < schedules.size - 1) {
                                                Spacer(modifier = Modifier.height(16.dp))
                                        }
                                }

                                if (schedules.size < 4) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        OutlinedButton(
                                                onClick = {
                                                        val lastEnd =
                                                                schedules.lastOrNull()?.end ?: 8
                                                        schedules =
                                                                schedules +
                                                                        BlackoutSchedule(
                                                                                minOf(
                                                                                        lastEnd + 2,
                                                                                        22
                                                                                ),
                                                                                minOf(
                                                                                        lastEnd + 4,
                                                                                        23
                                                                                )
                                                                        )
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors =
                                                        ButtonDefaults.outlinedButtonColors(
                                                                contentColor = Color(0xFF4CAF50)
                                                        ),
                                                border = BorderStroke(2.dp, Color(0xFF4CAF50))
                                        ) { Text("+ Agregar otro apagón") }
                                }
                        }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Impact preview
                val totalHours = schedules.sumOf { it.end - it.start }
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                        ImpactStat(
                                icon = Icons.Default.Warning,
                                value = "$totalHours horas",
                                label = "sin electricidad",
                                severity =
                                        when {
                                                totalHours > 12 -> "critical"
                                                totalHours > 8 -> "warning"
                                                else -> "normal"
                                        }
                        )
                        ImpactStat(
                                icon = Icons.Default.Battery1Bar,
                                value = "${24 - totalHours} horas",
                                label = "con electricidad",
                                severity = "normal"
                        )
                        ImpactStat(
                                icon = Icons.Default.Thermostat,
                                value =
                                        when {
                                                totalHours > 12 -> "Crítico"
                                                totalHours > 8 -> "Severo"
                                                else -> "Moderado"
                                        },
                                label = "Impacto en confort",
                                severity =
                                        when {
                                                totalHours > 12 -> "critical"
                                                totalHours > 8 -> "warning"
                                                else -> "normal"
                                        }
                        )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Warning/Success message
                if (day == 1) {
                        Card(
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0x33F44336)),
                                border = BorderStroke(1.dp, Color(0xFFFF6B6B))
                        ) {
                                Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Icon(
                                                Icons.Default.Info,
                                                contentDescription = null,
                                                tint = Color(0xFFFF6B6B)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                                text =
                                                        "Sin paneles solares, estos apagones afectarán severamente tu confort y productividad",
                                                color = Color.White
                                        )
                                }
                        }
                } else {
                        Card(
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0x334CAF50)),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50))
                        ) {
                                Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color(0xFF4CAF50)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                                text =
                                                        "Con tu sistema solar instalado, estos apagones no te afectarán",
                                                color = Color.White
                                        )
                                }
                        }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                        onClick = { onConfirm(schedules) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF667EEA))
                ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Comenzar Día $day", fontSize = 18.sp)
                }
        }
}

@Composable
fun ScheduleRow(
        index: Int,
        schedule: BlackoutSchedule,
        onStartChange: (Int) -> Unit,
        onEndChange: (Int) -> Unit,
        onRemove: (() -> Unit)?
) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                        text = "Apagón $index",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        modifier = Modifier.width(100.dp)
                )

                Text("Desde:", color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.width(8.dp))

                var startExpanded by remember { mutableStateOf(false) }
                Box {
                        OutlinedButton(
                                onClick = { startExpanded = true },
                                colors =
                                        ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color.White
                                        )
                        ) { Text("${schedule.start.toString().padStart(2, '0')}:00") }
                        DropdownMenu(
                                expanded = startExpanded,
                                onDismissRequest = { startExpanded = false }
                        ) {
                                (0..23).forEach { hour ->
                                        DropdownMenuItem(
                                                text = {
                                                        Text(
                                                                "${hour.toString().padStart(2, '0')}:00"
                                                        )
                                                },
                                                onClick = {
                                                        onStartChange(hour)
                                                        startExpanded = false
                                                }
                                        )
                                }
                        }
                }

                Spacer(modifier = Modifier.width(16.dp))
                Text("Hasta:", color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.width(8.dp))

                var endExpanded by remember { mutableStateOf(false) }
                Box {
                        OutlinedButton(
                                onClick = { endExpanded = true },
                                colors =
                                        ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color.White
                                        )
                        ) { Text("${schedule.end.toString().padStart(2, '0')}:00") }
                        DropdownMenu(
                                expanded = endExpanded,
                                onDismissRequest = { endExpanded = false }
                        ) {
                                (schedule.start + 1..23).forEach { hour ->
                                        DropdownMenuItem(
                                                text = {
                                                        Text(
                                                                "${hour.toString().padStart(2, '0')}:00"
                                                        )
                                                },
                                                onClick = {
                                                        onEndChange(hour)
                                                        endExpanded = false
                                                }
                                        )
                                }
                        }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Duration badge
                Surface(color = Color(0x33FFD700), shape = RoundedCornerShape(4.dp)) {
                        Text(
                                text = "${schedule.end - schedule.start}h",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold
                        )
                }

                Spacer(modifier = Modifier.weight(1f))

                onRemove?.let {
                        IconButton(onClick = it) {
                                Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Color(0xFFFF6B6B)
                                )
                        }
                }
        }
}

@Composable
fun ImpactStat(icon: ImageVector, value: String, label: String, severity: String) {
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))) {
                Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint =
                                        when (severity) {
                                                "critical" -> Color(0xFFFF6B6B)
                                                "warning" -> Color(0xFFFFA500)
                                                else -> Color(0xFF4CAF50)
                                        }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                text = value,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                        )
                        Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                        )
                }
        }
}

@Composable
fun SolarStudyScreen(appliances: Map<String, Appliance>, onComplete: (Int, Int) -> Unit) {
        var priorities by remember {
                mutableStateOf(
                        mapOf(
                                "refrigerator" to true,
                                "lights" to true,
                                "fan" to true,
                                "tv" to false,
                                "ac" to false,
                                "waterHeater" to false,
                                "computer" to false,
                                "phone" to true,
                                "washingMachine" to false,
                                "microwave" to false
                        )
                )
        }

        var budget by remember { mutableStateOf("medium") }
        var peopleCount by remember { mutableStateOf(4) }

        val requiredPower =
                priorities.entries.sumOf { (key, selected) ->
                        if (selected) appliances[key]?.consumption ?: 0 else 0
                }

        val dailyUsage = requiredPower * 8 / 1000f

        var panels = ceil(requiredPower / 505f).toInt()
        var batteries = ceil(dailyUsage / 5f).toInt()

        when (budget) {
                "low" -> {
                        panels = maxOf(2, (panels * 0.7f).toInt())
                        batteries = maxOf(1, (batteries * 0.6f).toInt())
                }
                "high" -> {
                        panels = (panels * 1.3f).toInt()
                        batteries = (batteries * 1.5f).toInt()
                }
        }

        if (peopleCount > 4) {
                panels = (panels * 1.2f).toInt()
                batteries = (batteries * 1.2f).toInt()
        } else if (peopleCount <= 2) {
                panels = maxOf(2, (panels * 0.8f).toInt())
                batteries = maxOf(1, (batteries * 0.8f).toInt())
        }

        panels = minOf(12, panels)
        batteries = minOf(6, batteries)

        val investment = panels * 800 + batteries * 1200
        val monthlyGeneration = panels * 505 * 5 * 30 / 1000
        val autonomy = batteries * 5000f / requiredPower

        LazyColumn(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp),
                                        tint = Color(0xFFFFD700)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                        text = "Estudio Solar Personalizado",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                )
                        }

                        Text(
                                text = "Diseñaremos el sistema perfecto para tus necesidades",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                        )
                }

                // Section 1: Household Info
                item {
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
                        ) {
                                Column(modifier = Modifier.padding(24.dp)) {
                                        Text(
                                                text = "1. Información del Hogar",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFD700)
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Text(
                                                text = "Personas en el hogar:",
                                                color = Color.White.copy(alpha = 0.9f)
                                        )

                                        var expanded by remember { mutableStateOf(false) }
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                                OutlinedButton(
                                                        onClick = { expanded = true },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        colors =
                                                                ButtonDefaults.outlinedButtonColors(
                                                                        contentColor = Color.White
                                                                )
                                                ) { Text("$peopleCount personas") }
                                                DropdownMenu(
                                                        expanded = expanded,
                                                        onDismissRequest = { expanded = false }
                                                ) {
                                                        (1..6).forEach { count ->
                                                                DropdownMenuItem(
                                                                        text = {
                                                                                Text(
                                                                                        if (count <
                                                                                                        6
                                                                                        )
                                                                                                "$count personas"
                                                                                        else
                                                                                                "6+ personas"
                                                                                )
                                                                        },
                                                                        onClick = {
                                                                                peopleCount = count
                                                                                expanded = false
                                                                        }
                                                                )
                                                        }
                                                }
                                        }

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Text(
                                                text = "Presupuesto disponible:",
                                                color = Color.White.copy(alpha = 0.9f)
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                                BudgetOption(
                                                        title = "Básico",
                                                        range = "$2,000-4,000",
                                                        selected = budget == "low",
                                                        onClick = { budget = "low" },
                                                        modifier = Modifier.weight(1f)
                                                )
                                                BudgetOption(
                                                        title = "Estándar",
                                                        range = "$4,000-8,000",
                                                        selected = budget == "medium",
                                                        onClick = { budget = "medium" },
                                                        modifier = Modifier.weight(1f)
                                                )
                                                BudgetOption(
                                                        title = "Premium",
                                                        range = "$8,000+",
                                                        selected = budget == "high",
                                                        onClick = { budget = "high" },
                                                        modifier = Modifier.weight(1f)
                                                )
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                }

                // Section 2: Priorities
                item {
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
                        ) {
                                Column(modifier = Modifier.padding(24.dp)) {
                                        Text(
                                                text =
                                                        "2. ¿Qué necesitas mantener encendido durante apagones?",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFD700)
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))

                                        LazyVerticalGrid(
                                                columns = GridCells.Adaptive(140.dp),
                                                modifier = Modifier.height(400.dp),
                                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                                items(appliances.entries.toList()) {
                                                        (key, appliance) ->
                                                        PriorityItem(
                                                                appliance = appliance,
                                                                selected = priorities[key] ?: false,
                                                                onClick = {
                                                                        priorities =
                                                                                priorities
                                                                                        .toMutableMap()
                                                                                        .apply {
                                                                                                this[
                                                                                                        key] =
                                                                                                        !(priorities[
                                                                                                                key]
                                                                                                                ?: false)
                                                                                        }
                                                                }
                                                        )
                                                }
                                        }

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Surface(
                                                color = Color(0x33FFD700),
                                                shape = RoundedCornerShape(8.dp)
                                        ) {
                                                Text(
                                                        text =
                                                                "Consumo total seleccionado: ${requiredPower}W",
                                                        modifier = Modifier.padding(16.dp),
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                )
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                }

                // Section 3: Recommendation
                item {
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                                border = BorderStroke(2.dp, Color(0xFFFFD700))
                        ) {
                                Column(modifier = Modifier.padding(24.dp)) {
                                        Text(
                                                text = "3. Sistema Recomendado para Ti",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFD700)
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                                RecommendationCard(
                                                        icon = Icons.Default.WbSunny,
                                                        value = panels.toString(),
                                                        label = "Paneles Solares",
                                                        detail = "505W c/u",
                                                        color = Color(0xFFFFD700),
                                                        modifier = Modifier.weight(1f)
                                                )
                                                RecommendationCard(
                                                        icon = Icons.Default.Battery1Bar,
                                                        value = batteries.toString(),
                                                        label = "Baterías",
                                                        detail = "5kWh c/u",
                                                        color = Color(0xFF4CAF50),
                                                        modifier = Modifier.weight(1f)
                                                )
                                                RecommendationCard(
                                                        icon = Icons.Default.AttachMoney,
                                                        value = "$${investment}",
                                                        label = "Inversión Total",
                                                        detail = "Financiamiento disponible",
                                                        color = Color(0xFF2196F3),
                                                        modifier = Modifier.weight(1f)
                                                )
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Card(
                                                colors =
                                                        CardDefaults.cardColors(
                                                                containerColor = Color(0x334CAF50)
                                                        )
                                        ) {
                                                Column(modifier = Modifier.padding(20.dp)) {
                                                        Text(
                                                                text = "Con este sistema podrás:",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .titleMedium,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF4CAF50)
                                                        )

                                                        Spacer(modifier = Modifier.height(12.dp))

                                                        BenefitItem(
                                                                "✅ Mantener todos los dispositivos seleccionados durante apagones"
                                                        )
                                                        BenefitItem(
                                                                "✅ ${autonomy.toInt()} horas de autonomía sin sol"
                                                        )
                                                        BenefitItem(
                                                                "✅ Generar ${monthlyGeneration} kWh mensuales"
                                                        )
                                                        BenefitItem(
                                                                "✅ Ahorrar ~$${(monthlyGeneration * 0.15f).toInt()} USD/mes"
                                                        )
                                                        BenefitItem(
                                                                "✅ ROI en ${(investment / (monthlyGeneration * 0.15f * 12)).toInt()} años"
                                                        )
                                                }
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                }

                // Actions
                item {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                OutlinedButton(
                                        onClick = { /* Manual config */},
                                        modifier = Modifier.weight(1f),
                                        colors =
                                                ButtonDefaults.outlinedButtonColors(
                                                        contentColor = Color.White
                                                )
                                ) { Text("Personalizar Manualmente") }
                                Button(
                                        onClick = { onComplete(panels, batteries) },
                                        modifier = Modifier.weight(1f),
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF4CAF50)
                                                )
                                ) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Aceptar y Continuar")
                                }
                        }
                }
        }
}

@Composable
fun BudgetOption(
        title: String,
        range: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
        Card(
                modifier = modifier.clickable { onClick() },
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        if (selected) Color(0x33FFD700) else Color(0xFF1A1A2E)
                        ),
                border = if (selected) BorderStroke(2.dp, Color(0xFFFFD700)) else null
        ) {
                Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(text = title, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                                text = range,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                        )
                }
        }
}

@Composable
fun PriorityItem(appliance: Appliance, selected: Boolean, onClick: () -> Unit) {
        Card(
                modifier = Modifier.fillMaxWidth().clickable { onClick() },
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        if (selected) Color(0x334CAF50) else Color(0xFF1A1A2E)
                        ),
                border = if (selected) BorderStroke(2.dp, Color(0xFF4CAF50)) else null
        ) {
                Box {
                        Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                IconWithColor(imageVector = appliance.icon, tint = appliance.iconColor, size = 24.dp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                        text = appliance.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                )
                                Text(
                                        text = "${appliance.consumption}W",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.7f)
                                )
                        }

                        if (selected) {
                                Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        modifier =
                                                Modifier.align(Alignment.TopEnd)
                                                        .padding(4.dp)
                                                        .size(16.dp),
                                        tint = Color(0xFF4CAF50)
                                )
                        }
                }
        }
}

@Composable
fun RecommendationCard(
        icon: ImageVector,
        value: String,
        label: String,
        detail: String,
        color: Color,
        modifier: Modifier = Modifier
) {
        Card(
                modifier = modifier,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
                border = BorderStroke(2.dp, color.copy(alpha = 0.5f))
        ) {
                Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = color
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                text = value,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = color
                        )
                        Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                        )
                        Text(
                                text = detail,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                        )
                }
        }
}

@Composable
fun BenefitItem(text: String) {
        Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
        )
}

@Composable
fun GameScreen(
        currentDay: Int,
        timeOfDay: Int,
        isBlackout: Boolean,
        energySource: EnergySource,
        solarPanels: Int,
        batteries: Int,
        currentSolarGeneration: Int,
        batteryCharge: Float,
        maxBatteryCapacity: Float,
        comfortLevel: Float,
        appliances: Map<String, Appliance>,
        foodSpoilage: Float,
        productivity: Float,
        hygiene: Float,
        temperature: Float,
        moneySaved: Float,
        co2Saved: Float,
        score: Int,
        gameSpeed: Long,
        onSpeedChange: (Long) -> Unit,
        onToggleAppliance: (String) -> Unit,
        onConfigureClick: () -> Unit
) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Header
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
                ) {
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                // Time Display
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                                imageVector =
                                                        if (timeOfDay in 6..17)
                                                                Icons.Default.WbSunny
                                                        else Icons.Default.Bedtime,
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                                tint =
                                                        if (timeOfDay in 6..17) Color(0xFFFFD700)
                                                        else Color(0xFF9C9CDB)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                                Text(
                                                        text =
                                                                "${timeOfDay.toString().padStart(2, '0')}:00",
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                )
                                                Text(
                                                        text = "Día $currentDay",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Color.White.copy(alpha = 0.8f)
                                                )
                                        }
                                }

                                // Energy Status
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                        EnergyIndicator(energySource)

                                        if (isBlackout) {
                                                Surface(
                                                        color = Color(0xFFFF6B6B),
                                                        shape = RoundedCornerShape(8.dp)
                                                ) {
                                                        Row(
                                                                modifier =
                                                                        Modifier.padding(
                                                                                horizontal = 12.dp,
                                                                                vertical = 6.dp
                                                                        ),
                                                                verticalAlignment =
                                                                        Alignment.CenterVertically
                                                        ) {
                                                                Icon(
                                                                        Icons.Default.Warning,
                                                                        contentDescription = null,
                                                                        modifier =
                                                                                Modifier.size(
                                                                                        16.dp
                                                                                ),
                                                                        tint = Color.White
                                                                )
                                                                Spacer(
                                                                        modifier =
                                                                                Modifier.width(4.dp)
                                                                )
                                                                Text(
                                                                        text = "APAGÓN ACTIVO",
                                                                        fontWeight =
                                                                                FontWeight.Bold,
                                                                        color = Color.White
                                                                )
                                                        }
                                                }
                                        }

                                        SpeedControl(gameSpeed, onSpeedChange)
                                }

                                // Metrics
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                                Icons.Default.Groups,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp),
                                                tint = Color.White.copy(alpha = 0.8f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                                text = "Comfort: ${comfortLevel.toInt()}%",
                                                color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        LinearProgressIndicator(
                                                progress = comfortLevel / 100f,
                                                modifier = Modifier.width(100.dp),
                                                color =
                                                        when {
                                                                comfortLevel < 30 ->
                                                                        Color(0xFFFF6B6B)
                                                                comfortLevel < 60 ->
                                                                        Color(0xFFFFA500)
                                                                else -> Color(0xFF4CAF50)
                                                        }
                                        )
                                }
                        }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        // Main House Area
                        Column(modifier = Modifier.weight(1f)) {
                                // Solar Display
                                if (solarPanels > 0) {
                                        Card(
                                                colors =
                                                        CardDefaults.cardColors(
                                                                containerColor = Color(0x33FFD700)
                                                        ),
                                                border = BorderStroke(1.dp, Color(0xFFFFD700))
                                        ) {
                                                Row(
                                                        modifier =
                                                                Modifier.fillMaxWidth()
                                                                        .padding(12.dp),
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        repeat(minOf(solarPanels, 6)) {
                                                                IconWithColor(imageVector = Icons.Default.WbSunny, tint = Color(0xFFFFD700), size = 24.dp)
                                                        }
                                                        Spacer(modifier = Modifier.weight(1f))
                                                        Text(
                                                                text =
                                                                        "${currentSolarGeneration}W generando",
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFFFFD700)
                                                        )
                                                }
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))
                                }

                                // Appliances Grid
                                Card(
                                        colors =
                                                CardDefaults.cardColors(
                                                        containerColor = Color(0xFF2A2A3E)
                                                )
                                ) {
                                        LazyVerticalGrid(
                                                columns = GridCells.Adaptive(150.dp),
                                                modifier = Modifier.padding(16.dp),
                                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                                items(appliances.entries.toList()) {
                                                        (key, appliance) ->
                                                        ApplianceCard(
                                                                appliance = appliance,
                                                                onClick = { onToggleAppliance(key) }
                                                        )
                                                }
                                        }
                                }
                        }

                        // Side Panel
                        Column(
                                modifier = Modifier.width(350.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                // Energy Flow
                                Card(
                                        colors =
                                                CardDefaults.cardColors(
                                                        containerColor = Color(0xFF2A2A3E)
                                                )
                                ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                        text = "Flujo de Energía",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFFFD700)
                                                )

                                                Spacer(modifier = Modifier.height(12.dp))

                                                FlowItem(
                                                        Icons.Default.WbSunny,
                                                        "Solar: ${currentSolarGeneration}W"
                                                )

                                                if (batteries > 0) {
                                                        FlowItem(
                                                                Icons.Default.Battery1Bar,
                                                                "Batería: ${(batteryCharge/1000).toInt()}/${(maxBatteryCapacity/1000).toInt()}kWh"
                                                        )

                                                        LinearProgressIndicator(
                                                                progress =
                                                                        batteryCharge /
                                                                                maxBatteryCapacity,
                                                                modifier =
                                                                        Modifier.fillMaxWidth()
                                                                                .padding(
                                                                                        vertical =
                                                                                                8.dp
                                                                                ),
                                                                color =
                                                                        when {
                                                                                batteryCharge <
                                                                                        maxBatteryCapacity *
                                                                                                0.2f ->
                                                                                        Color(
                                                                                                0xFFFF6B6B
                                                                                        )
                                                                                batteryCharge <
                                                                                        maxBatteryCapacity *
                                                                                                0.5f ->
                                                                                        Color(
                                                                                                0xFFFFA500
                                                                                        )
                                                                                else ->
                                                                                        Color(
                                                                                                0xFF4CAF50
                                                                                        )
                                                                        }
                                                        )

                                                        if (energySource == EnergySource.BATTERY) {
                                                                val totalConsumption =
                                                                        appliances.values.sumOf {
                                                                                if (it.on)
                                                                                        it.consumption
                                                                                else 0
                                                                        }
                                                                val autonomy =
                                                                        if (totalConsumption > 0) {
                                                                                batteryCharge /
                                                                                        totalConsumption
                                                                        } else 0f

                                                                Surface(
                                                                        color = Color(0x33FFD700),
                                                                        shape =
                                                                                RoundedCornerShape(
                                                                                        4.dp
                                                                                )
                                                                ) {
                                                                        Row(
                                                                                modifier =
                                                                                        Modifier.padding(
                                                                                                8.dp
                                                                                        ),
                                                                                verticalAlignment =
                                                                                        Alignment
                                                                                                .CenterVertically
                                                                        ) {
                                                                                Icon(
                                                                                        Icons.Default
                                                                                                .Schedule,
                                                                                        contentDescription =
                                                                                                null,
                                                                                        modifier =
                                                                                                Modifier.size(
                                                                                                        16.dp
                                                                                                ),
                                                                                        tint =
                                                                                                Color(
                                                                                                        0xFFFFD700
                                                                                                )
                                                                                )
                                                                                Spacer(
                                                                                        modifier =
                                                                                                Modifier.width(
                                                                                                        4.dp
                                                                                                )
                                                                                )
                                                                                Text(
                                                                                        text =
                                                                                                "Autonomía: ${autonomy.toInt()}h",
                                                                                        fontWeight =
                                                                                                FontWeight
                                                                                                        .Bold,
                                                                                        color =
                                                                                                Color(
                                                                                                        0xFFFFD700
                                                                                                )
                                                                                )
                                                                        }
                                                                }
                                                        }
                                                }

                                                val totalConsumption =
                                                        appliances.values.sumOf {
                                                                if (it.on) it.consumption else 0
                                                        }
                                                FlowItem(
                                                        Icons.Default.Bolt,
                                                        "Consumo: ${totalConsumption}W"
                                                )
                                        }
                                }

                                // Home Metrics
                                Card(
                                        colors =
                                                CardDefaults.cardColors(
                                                        containerColor = Color(0xFF2A2A3E)
                                                )
                                ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                        text = "Estado del Hogar",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFFFD700)
                                                )

                                                Spacer(modifier = Modifier.height(12.dp))

                                                MetricItem("Alimentos:", 100 - foodSpoilage)
                                                MetricItem("Productividad:", productivity)
                                                MetricItem("Higiene:", hygiene)

                                                Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement =
                                                                Arrangement.SpaceBetween,
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        Text(
                                                                "Temperatura:",
                                                                color = Color.White
                                                        )
                                                        Surface(
                                                                color =
                                                                        when {
                                                                                temperature > 32 ->
                                                                                        Color(
                                                                                                0x33FF6B6B
                                                                                        )
                                                                                temperature > 28 ->
                                                                                        Color(
                                                                                                0x33FFA500
                                                                                        )
                                                                                else ->
                                                                                        Color(
                                                                                                0x334CAF50
                                                                                        )
                                                                        },
                                                                shape = RoundedCornerShape(4.dp)
                                                        ) {
                                                                Text(
                                                                        text =
                                                                                "${temperature.toInt()}°C",
                                                                        modifier =
                                                                                Modifier.padding(
                                                                                        horizontal =
                                                                                                8.dp,
                                                                                        vertical =
                                                                                                4.dp
                                                                                ),
                                                                        fontWeight =
                                                                                FontWeight.Bold,
                                                                        color =
                                                                                when {
                                                                                        temperature >
                                                                                                32 ->
                                                                                                Color(
                                                                                                        0xFFFF6B6B
                                                                                                )
                                                                                        temperature >
                                                                                                28 ->
                                                                                                Color(
                                                                                                        0xFFFFA500
                                                                                                )
                                                                                        else ->
                                                                                                Color(
                                                                                                        0xFF4CAF50
                                                                                                )
                                                                                }
                                                                )
                                                        }
                                                }
                                        }
                                }

                                // Savings Display
                                Card(
                                        colors =
                                                CardDefaults.cardColors(
                                                        containerColor = Color(0xFF2A2A3E)
                                                )
                                ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                        text = "Impacto",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFFFD700)
                                                )

                                                Spacer(modifier = Modifier.height(12.dp))

                                                SavingItem(
                                                        Icons.Default.AttachMoney,
                                                        "Ahorrado: $${moneySaved.toString().take(5)}",
                                                        Color(0xFF4CAF50)
                                                )
                                                SavingItem(
                                                        Icons.Default.Thermostat,
                                                        "CO2: -${co2Saved.toInt()}kg",
                                                        Color(0xFF4CAF50)
                                                )
                                                SavingItem(
                                                        Icons.Default.EmojiEvents,
                                                        "Puntos: $score",
                                                        Color(0xFFFFD700)
                                                )
                                        }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Button(
                                        onClick = onConfigureClick,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFFFFD700)
                                                )
                                ) {
                                        Text(
                                                text =
                                                        if (currentDay == 1)
                                                                "Configurar Sistema Solar"
                                                        else "Ver Comparación",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                        )
                                }
                        }
                }
        }
}

@Composable
fun EnergyIndicator(energySource: EnergySource) {
        val (icon, text, color) =
                when (energySource) {
                        EnergySource.GRID ->
                                Triple(Icons.Default.Bolt, "Red eléctrica", Color(0xFF2196F3))
                        EnergySource.SOLAR ->
                                Triple(Icons.Default.WbSunny, "Panel solar", Color(0xFFFFD700))
                        EnergySource.BATTERY ->
                                Triple(Icons.Default.Battery1Bar, "Batería", Color(0xFF4CAF50))
                        EnergySource.NONE ->
                                Triple(Icons.Default.Warning, "Sin energía", Color(0xFFFF6B6B))
                }

        Surface(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, color)
        ) {
                Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = color
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = text, color = Color.White)
                }
        }
}

@Composable
fun SpeedControl(gameSpeed: Long, onSpeedChange: (Long) -> Unit) {
        var expanded by remember { mutableStateOf(false) }

        Box {
                OutlinedButton(
                        onClick = { expanded = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                        Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                                text =
                                        when (gameSpeed) {
                                                5000L -> "Lento"
                                                3000L -> "Normal"
                                                1500L -> "Rápido"
                                                else -> "Muy Rápido"
                                        },
                                fontSize = 12.sp
                        )
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                                text = { Text("Lento") },
                                onClick = {
                                        onSpeedChange(5000L)
                                        expanded = false
                                }
                        )
                        DropdownMenuItem(
                                text = { Text("Normal") },
                                onClick = {
                                        onSpeedChange(3000L)
                                        expanded = false
                                }
                        )
                        DropdownMenuItem(
                                text = { Text("Rápido") },
                                onClick = {
                                        onSpeedChange(1500L)
                                        expanded = false
                                }
                        )
                        DropdownMenuItem(
                                text = { Text("Muy Rápido") },
                                onClick = {
                                        onSpeedChange(500L)
                                        expanded = false
                                }
                        )
                }
        }
}

@Composable
fun ApplianceCard(appliance: Appliance, onClick: () -> Unit) {
        Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        when {
                                                appliance.on -> Color(0x334CAF50)
                                                appliance.essential && !appliance.on ->
                                                        Color(0x33FF6B6B)
                                                else -> Color(0xFF1A1A2E)
                                        }
                        ),
                border =
                        when {
                                appliance.on -> BorderStroke(2.dp, Color(0xFF4CAF50))
                                appliance.essential && !appliance.on ->
                                        BorderStroke(2.dp, Color(0xFFFF6B6B))
                                else -> null
                        }
        ) {
                Box(modifier = Modifier.padding(16.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconWithColor(imageVector = appliance.icon, tint = appliance.iconColor, size = 32.dp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                        text = appliance.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                )
                                Text(
                                        text = "${appliance.consumption}W",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.8f)
                                )
                        }

                        Surface(
                                modifier = Modifier.align(Alignment.TopEnd),
                                color = if (appliance.on) Color(0xFF4CAF50) else Color(0x33FFFFFF),
                                shape = CircleShape
                        ) {
                                Text(
                                        text = if (appliance.on) "✓" else "✗",
                                        modifier = Modifier.padding(4.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                )
                        }
                }
        }
}

@Composable
fun FlowItem(icon: ImageVector, text: String) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, color = Color.White)
        }
}

@Composable
fun MetricItem(label: String, value: Float) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                Text(text = label, modifier = Modifier.width(120.dp), color = Color.White)
                LinearProgressIndicator(
                        progress = value / 100f,
                        modifier = Modifier.weight(1f),
                        color =
                                when {
                                        value < 30 -> Color(0xFFFF6B6B)
                                        value < 60 -> Color(0xFFFFA500)
                                        else -> Color(0xFF4CAF50)
                                }
                )
        }
}

@Composable
fun SavingItem(icon: ImageVector, text: String, color: Color) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = color
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, color = Color.White)
        }
}

@Composable
fun ConfigurationScreen(onApply: (Int, Int) -> Unit) {
        var panels by remember { mutableStateOf(4) }
        var batteries by remember { mutableStateOf(2) }
        var showHelper by remember { mutableStateOf(true) }

        val investment = panels * 800 + batteries * 1200
        val monthlyGeneration = panels * 505 * 5 * 30 / 1000
        val monthlySavings = monthlyGeneration * 0.15f
        val roi = investment / (monthlySavings * 12)

        Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(
                        text = "Configura Tu Sistema Solar",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                )

                Text(
                        text = "Diseña tu independencia energética",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                // Helper Box
                AnimatedVisibility(visible = showHelper) {
                        Card(
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0x332196F3)),
                                border = BorderStroke(2.dp, Color(0xFF2196F3))
                        ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                Row(
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        Icon(
                                                                Icons.Default.Info,
                                                                contentDescription = null,
                                                                tint = Color(0xFF4FC3F7)
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                                text = "Guía de Configuración",
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF4FC3F7)
                                                        )
                                                }
                                                IconButton(onClick = { showHelper = false }) {
                                                        Icon(
                                                                Icons.Default.Close,
                                                                contentDescription = null,
                                                                tint =
                                                                        Color.White.copy(
                                                                                alpha = 0.7f
                                                                        )
                                                        )
                                                }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        HelperSection(
                                                "Casa Pequeña (1-2 personas):",
                                                "2-3 paneles + 1-2 baterías = $2,800-4,000"
                                        )
                                        HelperSection(
                                                "🏡 Casa Mediana (3-4 personas):",
                                                "4-6 paneles + 2-3 baterías = $5,600-8,400"
                                        )
                                        HelperSection(
                                                "Casa Grande (5+ personas):",
                                                "7-10 paneles + 3-4 baterías = $10,400-12,800"
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Surface(
                                                color = Color(0x33FFD700),
                                                shape = RoundedCornerShape(8.dp)
                                        ) {
                                                Row(
                                                        modifier = Modifier.padding(12.dp),
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        IconWithColor(imageVector = Icons.Default.Lightbulb, tint = Color(0xFFFFD700), size = 20.dp)
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                                text =
                                                                        "Consejo: Cada panel genera ~75 kWh/mes. Una casa promedio consume 200-300 kWh/mes.",
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color.White
                                                        )
                                                }
                                        }
                                }
                        }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Configuration
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))) {
                        Column(modifier = Modifier.padding(24.dp)) {
                                // Solar Panels
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                                Icons.Default.WbSunny,
                                                contentDescription = null,
                                                tint = Color(0xFFFFD700)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                                text = "Paneles Solares JA Solar 505W",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFD700)
                                        )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Slider(
                                                value = panels.toFloat(),
                                                onValueChange = { panels = it.toInt() },
                                                valueRange = 0f..12f,
                                                modifier = Modifier.weight(1f),
                                                colors =
                                                        SliderDefaults.colors(
                                                                thumbColor = Color(0xFFFFD700),
                                                                activeTrackColor = Color(0xFFFFD700)
                                                        )
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                                text = "$panels paneles",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )
                                }

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                        Text(
                                                text = "Generación pico: ${panels * 505}W",
                                                color = Color.White.copy(alpha = 0.8f)
                                        )
                                        Text(
                                                text =
                                                        "Generación mensual: ${monthlyGeneration} kWh",
                                                color = Color.White.copy(alpha = 0.8f)
                                        )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Batteries
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                                Icons.Default.Battery1Bar,
                                                contentDescription = null,
                                                tint = Color(0xFF4CAF50)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                                text = "Baterías Pylontech 5kWh",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF4CAF50)
                                        )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Slider(
                                                value = batteries.toFloat(),
                                                onValueChange = { batteries = it.toInt() },
                                                valueRange = 0f..6f,
                                                modifier = Modifier.weight(1f),
                                                colors =
                                                        SliderDefaults.colors(
                                                                thumbColor = Color(0xFF4CAF50),
                                                                activeTrackColor = Color(0xFF4CAF50)
                                                        )
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                                text = "$batteries baterías",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )
                                }

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                        Text(
                                                text = "Capacidad total: ${batteries * 5}kWh",
                                                color = Color.White.copy(alpha = 0.8f)
                                        )
                                        Text(
                                                text =
                                                        "Autonomía nocturna: ${(batteries * 5 / 3).toInt()} horas",
                                                color = Color.White.copy(alpha = 0.8f)
                                        )
                                }

                                if (batteries < 2) {
                                        Surface(
                                                color = Color(0x33FFA500),
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                                Text(
                                                        text =
                                                                "Recomendamos mínimo 2 baterías para pasar la noche",
                                                        modifier = Modifier.padding(8.dp),
                                                        color = Color(0xFFFFA500)
                                                )
                                        }
                                }
                        }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Investment Summary
                Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                        border = BorderStroke(1.dp, Color(0xFFFFD700))
                ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                        text = "Resumen de Inversión",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                        SummaryItem("Inversión Total:", "$${investment}")
                                        SummaryItem("Ahorro Mensual:", "$${monthlySavings.toInt()}")
                                        SummaryItem("Retorno (ROI):", "${roi.toInt()} años")
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Surface(
                                        color = Color(0x334CAF50),
                                        shape = RoundedCornerShape(8.dp)
                                ) {
                                        Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                IconWithColor(imageVector = Icons.Default.CreditCard, tint = Color(0xFF4CAF50), size = 20.dp)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                        text =
                                                                "Financiamiento disponible: Desde $200/mes con planes flexibles",
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                )
                                        }
                                }
                        }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                        onClick = { onApply(panels, batteries) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        enabled = panels > 0 || batteries > 0
                ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                                text =
                                        if (panels == 0 && batteries == 0)
                                                "Selecciona al menos 1 panel"
                                        else "Instalar y Continuar",
                                fontSize = 18.sp
                        )
                }
        }
}

@Composable
fun NotificationCard(notification: Notification) {
        val backgroundColor =
                when (notification.type) {
                        NotificationType.SUCCESS -> Color(0x334CAF50)
                        NotificationType.WARNING -> Color(0x33FFA500)
                        NotificationType.ERROR -> Color(0x33FF6B6B)
                        NotificationType.INFO -> Color(0x332196F3)
                }

        val borderColor =
                when (notification.type) {
                        NotificationType.SUCCESS -> Color(0xFF4CAF50)
                        NotificationType.WARNING -> Color(0xFFFFA500)
                        NotificationType.ERROR -> Color(0xFFFF6B6B)
                        NotificationType.INFO -> Color(0xFF2196F3)
                }

        val icon =
                when (notification.type) {
                        NotificationType.SUCCESS -> Icons.Default.Check
                        NotificationType.WARNING -> Icons.Default.Warning
                        NotificationType.ERROR -> Icons.Default.Error
                        NotificationType.INFO -> Icons.Default.Info
                }

        Card(
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier.animateContentSize()
        ) {
                Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = borderColor,
                                        modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                        text = notification.message,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                )
                        }

                        notification.recommendation?.let { recommendation ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                        text = recommendation,
                                        color = Color.White.copy(alpha = 0.8f),
                                        style = MaterialTheme.typography.bodySmall
                                )
                        }
                }
        }
}

@Composable
fun EventPopup(event: Event) {
        Dialog(onDismissRequest = { /* Handle dismiss */}) {
                Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E)),
                        border = BorderStroke(2.dp, Color(0xFFFFD700))
                ) {
                        Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                        text = event.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                        text = event.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        OutlinedButton(
                                                onClick = { /* Handle dismiss */},
                                                colors =
                                                        ButtonDefaults.outlinedButtonColors(
                                                                contentColor = Color.White
                                                        )
                                        ) { Text("Cancelar") }

                                        Button(
                                                onClick = {
                                                        event.effect()
                                                        /* Handle dismiss */
                                                },
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFFFFD700)
                                                        )
                                        ) { Text("Aceptar", color = Color.Black) }
                                }
                        }
                }
        }
}

@Composable
fun AchievementBadge(achievement: Achievement) {
        Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                border = BorderStroke(2.dp, Color(0xFFFFD700))
        ) {
                Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        IconWithColor(imageVector = Icons.Default.EmojiEvents, tint = Color(0xFFFFD700), size = 24.dp)

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                                Text(
                                        text = achievement.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                )
                                Text(
                                        text = achievement.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.8f)
                                )
                        }
                }
        }
}

@Composable
fun ComparisonScreen(
        comparison: Map<String, ComparisonData?>,
        score: Int,
        onShare: () -> Unit,
        onQuote: () -> Unit
) {
        val withoutSolar = comparison["withoutSolar"]
        val withSolar = comparison["withSolar"]

        Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(
                        text = "Comparación de Resultados",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                )

                Text(
                        text = "Ve cómo cambió tu experiencia con energía solar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                // Comparison Cards
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        // Without Solar
                        Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF3E3E52))
                        ) {
                                Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp),
                                                tint = Color(0xFFFF6B6B)
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                                text = "Sin Paneles Solares",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        ComparisonMetric(
                                                "Comfort",
                                                withoutSolar?.comfort ?: 0f,
                                                "%"
                                        )
                                        ComparisonMetric(
                                                "Dinero Gastado",
                                                withoutSolar?.moneySpent ?: 0f,
                                                "$"
                                        )
                                        ComparisonMetric(
                                                "Productividad",
                                                withoutSolar?.productivity ?: 0f,
                                                "%"
                                        )
                                        ComparisonMetric(
                                                "Alimentos Perdidos",
                                                withoutSolar?.foodLost ?: 0f,
                                                "%"
                                        )
                                }
                        }

                        // With Solar
                        Card(
                                modifier = Modifier.weight(1f),
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0xFF3A5F3A)),
                                border = BorderStroke(2.dp, Color(0xFFFFD700))
                        ) {
                                Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.WbSunny,
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp),
                                                tint = Color(0xFFFFD700)
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                                text = "Con Paneles Solares",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        ComparisonMetric("Comfort", withSolar?.comfort ?: 0f, "%")
                                        ComparisonMetric(
                                                "Dinero Ahorrado",
                                                withSolar?.moneySaved ?: 0f,
                                                "$"
                                        )
                                        ComparisonMetric(
                                                "CO2 Ahorrado",
                                                withSolar?.co2Saved ?: 0f,
                                                "kg"
                                        )
                                }
                        }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Score and Actions
                Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                        border = BorderStroke(2.dp, Color(0xFFFFD700))
                ) {
                        Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                Text(
                                        text = "Puntuación Final",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                        text = "$score puntos",
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        OutlinedButton(
                                                onClick = onShare,
                                                colors =
                                                        ButtonDefaults.outlinedButtonColors(
                                                                contentColor = Color.White
                                                        ),
                                                border = BorderStroke(1.dp, Color.White)
                                        ) {
                                                Icon(Icons.Default.Send, contentDescription = null)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Compartir")
                                        }

                                        Button(
                                                onClick = onQuote,
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFFFFD700)
                                                        )
                                        ) {
                                                Icon(
                                                        Icons.Default.Description,
                                                        contentDescription = null
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Solicitar Cotización", color = Color.Black)
                                        }
                                }
                        }
                }
        }
}

@Composable
fun ComparisonMetric(label: String, value: Float, unit: String) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
                Text(text = label, color = Color.White.copy(alpha = 0.8f))
                Text(
                        text = "${value.toString().take(4)}$unit",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                )
        }
}

@Composable
fun HelperSection(title: String, description: String) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.width(200.dp)
                )
                Text(text = description, color = Color.White.copy(alpha = 0.8f))
        }
}

@Composable
fun SummaryItem(label: String, value: String) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                        text = value,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                )
        }
}
