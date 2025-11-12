package com.suncar.solarsurvivor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.*
import com.suncar.solarsurvivor.ui.screens.*
import com.suncar.solarsurvivor.ui.components.atom.EventPopup
import com.suncar.solarsurvivor.ui.components.atom.SituationDialog
import com.suncar.solarsurvivor.ui.components.molecules.AchievementBadge
import com.suncar.solarsurvivor.ui.components.organisms.NotificationCard
import kotlin.random.Random
import kotlinx.coroutines.delay

@Composable
fun SolarSurvivorGame() {
        // Inicializar GameStatsCollector al comenzar
        LaunchedEffect(Unit) {
            GameStatsCollector.startNewSession()
        }

        var gameState by remember {
            mutableStateOf(
                GameState.START
            )
        }
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
        var connectivity by remember { mutableStateOf(100f) }
        var waterSupply by remember { mutableStateOf(100f) }
        var security by remember { mutableStateOf(100f) }

        // Electrodomésticos
        var appliances by remember {
                mutableStateOf(
                        mapOf(
                                "refrigerator" to
                                        Appliance("Refrigerador", Icons.Default.Kitchen, Color(0xFF4FC3F7), false, 1, 150),
                                "lights" to Appliance("Iluminación", Icons.Default.Lightbulb, Color(0xFFFFD700), false, 2, 60),
                                "waterPump" to Appliance("Bomba de Agua", Icons.Default.WaterDrop, Color(0xFF2196F3), false, 3, 75),
                                "fan" to Appliance("Ventilador", Icons.Default.Air, Color(0xFF81C784), false, 4, 75),
                                "router" to Appliance("Router/WiFi", Icons.Default.Router, Color(0xFF607D8B), false, 5, 25),
                                "tv" to Appliance("Televisor", Icons.Default.Tv, Color(0xFF9C27B0), false, 6, 100),
                                "ac" to Appliance("Aire Acondicionado", Icons.Default.AcUnit, Color(0xFF29B6F6), false, 7, 1500),
                                "waterHeater" to Appliance("Calentador", Icons.Default.Whatshot, Color(0xFFFF5722), false, 8, 1200),
                                "computer" to Appliance("Computadora", Icons.Default.Computer, Color(0xFF757575), false, 9, 300),
                                "phone" to Appliance("Cargador Móvil", Icons.Default.PhoneAndroid, Color(0xFF4CAF50), false, 10, 10),
                                "washingMachine" to Appliance("Lavadora", Icons.Default.LocalLaundryService, Color(0xFF03A9F4), false, 11, 500),
                                "microwave" to Appliance("Microondas", Icons.Default.Microwave, Color(0xFFFF9800), false, 12, 800),
                                "securitySystem" to Appliance("Seguridad", Icons.Default.Security, Color(0xFFE91E63), false, 13, 40)
                        )
                )
        }

        // Eventos y notificaciones
        var currentEvent by remember { mutableStateOf<Event?>(null) }
        var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
        var achievements by remember { mutableStateOf<List<Achievement>>(emptyList()) }
        var score by remember { mutableStateOf(0) }
        val sessionSummary by GameStatsCollector.sessionSummary

        // Sistema de situaciones y puntuación
        var currentSituation by remember { mutableStateOf<Situation?>(null) }
        var lastSituationHour by remember { mutableStateOf(-1) }
        var dailySituationsShown by remember { mutableStateOf(0) }
        var isPausedBySituation by remember { mutableStateOf(false) }
        var dailyComfortSum by remember { mutableStateOf(0f) }
        var dailyComfortCount by remember { mutableStateOf(0) }

        // Configuración y estadísticas
        var selectedHome by remember { mutableStateOf("medium") }
        var dayStats by remember { mutableStateOf(DayStats()) }
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

        fun calculateRealTemperature(): Float {
                // Temperatura base según hora del día
                val baseTemp = when {
                        timeOfDay in 0..5 -> 22f + Random.nextFloat() * 2f     // Madrugada: 22-24°C
                        timeOfDay in 6..11 -> 25f + Random.nextFloat() * 3f    // Mañana: 25-28°C
                        timeOfDay in 12..16 -> 30f + Random.nextFloat() * 5f   // Mediodía: 30-35°C
                        timeOfDay in 17..20 -> 28f + Random.nextFloat() * 2f   // Tarde: 28-30°C
                        else -> 26f + Random.nextFloat() * 2f                  // Noche: 26-28°C
                }

                // Efectos de electrodomésticos de climatización
                var tempReduction = 0f
                if (appliances["fan"]?.on == true) {
                        tempReduction += 4f // Ventilador reduce sensación térmica
                }
                if (appliances["ac"]?.on == true) {
                        tempReduction += 10f // AC reduce temperatura real
                }

                return maxOf(18f, baseTemp - tempReduction) // Mínimo 18°C
        }

        fun calculateConnectivity(): Float {
                var connectivity = 0f
                if (appliances["phone"]?.on == true) connectivity += 40f
                if (appliances["computer"]?.on == true) connectivity += 40f
                if (appliances["router"]?.on == true) connectivity += 20f
                return minOf(100f, connectivity)
        }

        fun calculateWaterSupply(): Float {
                return if (appliances["waterPump"]?.on == true) 100f else 0f
        }

        fun calculateSecurity(): Float {
                var security = 50f // Base sin electricidad
                if (appliances["lights"]?.on == true) security += 30f
                if (appliances["securitySystem"]?.on == true) security += 20f
                return minOf(100f, security)
        }

        fun calculateAvailableEnergy(): Int {
                return currentSolarGeneration +
                        (if (isBlackout) 0 else Int.MAX_VALUE) + // Energía de red ilimitada cuando no hay blackout
                        (if (batteryCharge > 0) 1000 else 0)
        }

        fun autoTurnOffAppliancesByPriority() {
                if (!isBlackout) return // No apagar nada si hay corriente de calle
                
                val totalConsumption = calculateTotalConsumption()
                val availableEnergy = currentSolarGeneration + (if (batteryCharge > 0) 1000 else 0)
                
                if (totalConsumption <= availableEnergy) return
                
                // Obtener dispositivos encendidos ordenados por prioridad (mayor prioridad = número más alto)
                // Los de menor prioridad se apagan primero
                val onAppliances = appliances.filter { it.value.on }
                        .toList()
                        .sortedBy { it.second.priority } // Ordenar de menor a mayor prioridad
                
                var currentConsumption = totalConsumption
                val appliancesToTurnOff = mutableListOf<String>()
                
                for ((key, appliance) in onAppliances) {
                        if (currentConsumption <= availableEnergy) break
                        
                        appliancesToTurnOff.add(key)
                        currentConsumption -= appliance.consumption
                }
                
                // Apagar los dispositivos necesarios
                if (appliancesToTurnOff.isNotEmpty()) {
                        val updatedAppliances = appliances.toMutableMap()
                        val turnedOffNames = mutableListOf<String>()
                        
                        appliancesToTurnOff.forEach { key ->
                                updatedAppliances[key]?.let { appliance ->
                                        updatedAppliances[key] = appliance.copy(on = false)
                                        turnedOffNames.add(appliance.name)
                                }
                        }
                        
                        appliances = updatedAppliances
                        
                        val deficit = totalConsumption - availableEnergy
                        notifications = notifications + Notification(
                                kotlin.random.Random.nextLong(),
                                "⚠️ Energía insuficiente - Apagado automático",
                                NotificationType.WARNING,
                                "❌ Se apagaron: ${turnedOffNames.joinToString(", ")}. Déficit: ${deficit}W"
                        )
                }
        }

        fun addNotification(
                message: String,
                type: NotificationType = NotificationType.INFO,
                recommendation: String? = null
        ) {
                val id = Random.nextLong()
                notifications = notifications + Notification(id, message, type, recommendation)
        }
        
        fun tryGenerateSituation() {
                // Solo 2 situaciones por día: a las 10:00 y 16:00
                if (currentSituation != null) return // Ya hay una situación activa
                if (dailySituationsShown >= 2) return // Máximo 2 por día

                // Horarios fijos para situaciones
                val shouldShowSituation = when (dailySituationsShown) {
                        0 -> timeOfDay == 10 // Primera situación a las 10:00
                        1 -> timeOfDay == 16 // Segunda situación a las 16:00
                        else -> false
                }

                if (!shouldShowSituation) return

                val situation = SituationsDatabase.getRandomSituationWeighted()
                if (situation != null) {
                        currentSituation = situation
                        dailySituationsShown++
                        isPausedBySituation = true // Pausar el juego
                }
        }

        fun handleSituationAnswer(option: SituationOption) {
                val situation = currentSituation ?: return

                val pointsEarned = if (option.isCorrect) situation.points else 0
                score += pointsEarned

                if (option.isCorrect) {
                        addNotification(
                                "¡Respuesta correcta! +${situation.points} puntos",
                                NotificationType.SUCCESS,
                                "Has demostrado conocimiento en gestión de energía solar"
                        )
                } else {
                        addNotification(
                                "Respuesta incorrecta",
                                NotificationType.INFO,
                                "Aprende de esta situación para mejorar tu gestión energética"
                        )
                }

                currentSituation = null
                isPausedBySituation = false // Reanudar el juego
        }

        fun calculateDailyComfortBonus(): Int {
                if (dailyComfortCount == 0) return 0
                val averageComfort = dailyComfortSum / dailyComfortCount

                // Bonificación por confort promedio:
                // 90-100: 15 puntos
                // 80-89: 10 puntos
                // 70-79: 5 puntos
                // < 70: 0 puntos
                return when {
                        averageComfort >= 90f -> 15
                        averageComfort >= 80f -> 10
                        averageComfort >= 70f -> 5
                        else -> 0
                }
        }

        fun checkAndGenerateContextualNotifications() {
                // Solo generar notificaciones cada 2 horas para evitar spam
                if (timeOfDay % 2 != 0) return
                
                // Notificación sobre confort bajo
                if (comfortLevel < 40 && currentDay == 1 && notifications.none { it.message.contains("confort") }) {
                        addNotification(
                                "😤 FAMILIA FRUSTRADA",
                                NotificationType.WARNING,
                                if (solarPanels == 0)
                                        "💡 La familia necesita más comodidades. Enciende dispositivos esenciales como luces y ventilador"
                                else
                                        "🌞 Usa tu energía solar para mejorar las condiciones. Enciende aire acondicionado o más dispositivos"
                        )
                }
                
                // Notificación sobre generación solar máxima
                if (solarPanels > 0 && timeOfDay in 10..13 && currentSolarGeneration > 0 && 
                    notifications.none { it.message.contains("solar máxima") }) {
                        val excess = currentSolarGeneration - appliances.values.sumOf { if (it.on) it.consumption else 0 }
                        if (excess > 500) {
                                addNotification(
                                        "☀️ GENERACIÓN SOLAR MÁXIMA",
                                        NotificationType.SUCCESS,
                                        "🔋 Generando ${excess}W extra. Momento ideal para encender más electrodomésticos o cargar baterías"
                                )
                        }
                }
                
                // Notificación sobre batería baja durante apagón
                if (isBlackout && batteries > 0 && batteryCharge < maxBatteryCapacity * 0.25f &&
                    notifications.none { it.message.contains("batería baja") }) {
                        addNotification(
                                "🔋 BATERÍA BAJA DURANTE APAGÓN",
                                NotificationType.WARNING,
                                "⚡ Quedan ${(batteryCharge/1000).toInt()}kWh. Apaga dispositivos no esenciales para conservar energía"
                        )
                }
                
                // Sugerencia sobre ahorro de energía
                if (currentDay == 1 && solarPanels == 0 && timeOfDay == 16 && 
                    appliances.values.count { it.on } > 5 &&
                    notifications.none { it.message.contains("muchos dispositivos") }) {
                        addNotification(
                                "💡 CONSEJO DE EFICIENCIA",
                                NotificationType.INFO,
                                "⚡ Tienes muchos dispositivos encendidos. Con paneles solares podrías mantenerlos todos sin preocupación"
                        )
                }
        }

        fun toggleAppliance(appKey: String) {
                if (gameState != GameState.PLAYING) return

                val app = appliances[appKey] ?: return
                val consumption = calculateTotalConsumption()
                val available = if (isBlackout) {
                        // Durante blackout: solo solar + baterías
                        currentSolarGeneration + (if (batteryCharge > 0) 1000 else 0)
                } else {
                        // Con corriente de calle: prácticamente ilimitada
                        Int.MAX_VALUE
                }

        if (!app.on && consumption + app.consumption > available) {
                return
        }

        appliances =
                appliances.toMutableMap().apply { this[appKey] = app.copy(on = !app.on) }
        }

        // Game Loop Effect
        LaunchedEffect(gameState, dayStarted, isPausedBySituation) {
                if (gameState == GameState.PLAYING && dayStarted && !isPausedBySituation) {
                        while (true) {
                                delay(gameSpeed)

                                // Update time
                                timeOfDay = (timeOfDay + 1) % 24

                                if (timeOfDay == 0) {
                                        // Calcular bonificación por confort promedio del día
                                        val comfortBonus = calculateDailyComfortBonus()
                                        if (comfortBonus > 0) {
                                                score += comfortBonus
                                                addNotification(
                                                        "¡Bonificación del día! +${comfortBonus} puntos",
                                                        NotificationType.SUCCESS,
                                                        "Confort promedio del día: ${(dailyComfortSum / dailyComfortCount).toInt()}%"
                                                )
                                        }

                                        // Capturar estadísticas al final del día
                                        val blackoutHoursToday = blackoutSchedule.sumOf {
                                            maxOf(0, minOf(it.end, 24) - maxOf(it.start, 0))
                                        }
                                        val hourlyStatsToday = GameStatsCollector.hourlyStats.filter { it.day == currentDay }
                                        val minComfortToday = hourlyStatsToday.minOfOrNull { it.comfortLevel } ?: comfortLevel
                                        val maxSolarToday = hourlyStatsToday.maxOfOrNull { it.solarGeneration } ?: currentSolarGeneration
                                        val totalEnergyToday = hourlyStatsToday.sumOf { it.totalConsumption }
                                        val criticalEventsToday = GameStatsCollector.gameEvents.count {
                                            it.day == currentDay && (it.type.contains("ERROR") || it.type.contains("BLACKOUT"))
                                        }

                                        GameStatsCollector.captureDayEndStats(
                                            day = currentDay,
                                            finalComfortLevel = comfortLevel,
                                            totalBlackoutHours = blackoutHoursToday,
                                            averageTemperature = hourlyStatsToday.map { it.temperature }.average().toFloat(),
                                            minComfortLevel = minComfortToday,
                                            maxSolarGeneration = maxSolarToday,
                                            totalEnergyConsumed = totalEnergyToday,
                                            criticalEventsCount = criticalEventsToday
                                        )

                                        // Reset daily comfort tracking
                                        dailyComfortSum = 0f
                                        dailyComfortCount = 0

                                        // Reset daily situations counter
                                        dailySituationsShown = 0

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
                                        
                                        // Registrar evento de apagón
                                        GameStatsCollector.addGameEvent(
                                            "BLACKOUT_START",
                                            "Apagón iniciado - Duración: $duration horas",
                                            mapOf(
                                                "duration" to duration,
                                                "hasSolar" to (solarPanels > 0),
                                                "batteryCharge" to batteryCharge,
                                                "comfortLevel" to comfortLevel
                                            )
                                        )
                                        
                                        addNotification(
                                                "⚡ APAGÓN INICIADO - Duración: $duration horas",
                                                NotificationType.ERROR,
                                                if (solarPanels > 0)
                                                        "✅ Tu sistema solar te protege. Úsalo con confianza durante el apagón"
                                                else
                                                        "💡 Conserva energía: mantén encendido solo el refrigerador y dispositivos esenciales"
                                        )
                                } else if (!inBlackout && isBlackout) {
                                        isBlackout = false
                                        
                                        // Registrar evento de restauración de energía
                                        GameStatsCollector.addGameEvent(
                                            "BLACKOUT_END",
                                            "Electricidad restaurada",
                                            mapOf(
                                                "hasSolar" to (solarPanels > 0),
                                                "batteryCharge" to batteryCharge,
                                                "comfortLevel" to comfortLevel
                                            )
                                        )
                                        
                                        addNotification(
                                                "🔌 ELECTRICIDAD RESTAURADA",
                                                NotificationType.SUCCESS,
                                                if (solarPanels > 0)
                                                        "Ahora tienes energía solar + red eléctrica. Momento perfecto para cargar baterías"
                                                else
                                                        "Aprovecha para encender electrodomésticos y recuperar el confort familiar"
                                        )
                                }

                                // Update solar generation
                                currentSolarGeneration = calculateSolarGeneration()

                                // Check if we need to automatically turn off appliances due to insufficient energy
                                autoTurnOffAppliancesByPriority()

                                // Update all home metrics
                                temperature = calculateRealTemperature()
                                connectivity = calculateConnectivity()
                                waterSupply = calculateWaterSupply()
                                security = calculateSecurity()

                                // Update food spoilage
                                if (!appliances["refrigerator"]?.on!!) {
                                        foodSpoilage = minOf(100f, foodSpoilage + 5f)
                                } else {
                                        foodSpoilage = maxOf(0f, foodSpoilage - 2f)
                                }

                                // Update productivity
                                if (!appliances["lights"]?.on!! && (timeOfDay < 6 || timeOfDay > 18)) {
                                        productivity = maxOf(0f, productivity - 3f)
                                } else if (appliances["lights"]?.on!! || (timeOfDay in 6..18)) {
                                        productivity = minOf(100f, productivity + 1f)
                                }

                                // Update hygiene
                                if (!appliances["waterHeater"]?.on!! || waterSupply < 50) {
                                        hygiene = maxOf(0f, hygiene - 2f)
                                } else {
                                        hygiene = minOf(100f, hygiene + 0.5f)
                                }

                                // Calculate comfort based on multiple realistic factors
                                val temperatureComfort = when {
                                        temperature in 20f..26f -> 2f    // Ideal
                                        temperature in 26f..30f -> 0f    // Cómodo
                                        temperature in 30f..35f -> -3f   // Caluroso
                                        temperature > 35f -> -6f         // Extremo
                                        else -> -2f                      // Frío
                                }

                                val connectivityComfort = when {
                                        connectivity >= 80 -> 1f
                                        connectivity >= 40 -> 0f
                                        else -> -2f
                                }

                                val waterComfort = when {
                                        waterSupply >= 100 -> 1f
                                        waterSupply >= 50 -> -1f
                                        else -> -4f
                                }

                                val securityComfort = when {
                                        security >= 80 -> 1f
                                        security >= 50 -> 0f
                                        else -> -2f
                                }

                                val foodComfort = when {
                                        foodSpoilage < 20 -> 1f
                                        foodSpoilage < 50 -> 0f
                                        else -> -3f
                                }

                                val energySourceComfort = when {
                                        energySource == EnergySource.NONE -> -5f
                                        isBlackout && (energySource == EnergySource.SOLAR || energySource == EnergySource.BATTERY) -> 2f
                                        else -> 0f
                                }

                                // Update comfort level with all factors
                                comfortLevel = maxOf(
                                        0f,
                                        minOf(
                                                100f,
                                                comfortLevel + temperatureComfort + connectivityComfort + 
                                                waterComfort + securityComfort + foodComfort + energySourceComfort
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
                                
                                // Acumular confort para bonificación diaria
                                dailyComfortSum += comfortLevel
                                dailyComfortCount++

                                // Generar notificaciones contextuales inteligentes
                                checkAndGenerateContextualNotifications()

                                // Intentar generar situaciones educativas
                                tryGenerateSituation()

                                // Capturar estadísticas cada hora
                                GameStatsCollector.captureHourlyStats(
                                    currentDay = currentDay,
                                    timeOfDay = timeOfDay,
                                    isBlackout = isBlackout,
                                    energySource = energySource,
                                    solarGeneration = currentSolarGeneration,
                                    batteryCharge = batteryCharge,
                                    comfortLevel = comfortLevel,
                                    appliances = appliances,
                                    temperature = temperature,
                                    connectivity = connectivity,
                                    waterSupply = waterSupply,
                                    security = security,
                                    foodSpoilage = foodSpoilage,
                                    productivity = productivity,
                                    hygiene = hygiene
                                )
                        }
                }
        }

        // Main UI
        MaterialTheme(
                colorScheme =
                        darkColorScheme(
                                primary = Color(0xFF0F2B66),
                                secondary = Color(0xFFF26729),
                                tertiary = Color(0xFFFDB813),
                                background = Color(0xFF0A1B40),
                                surface = Color(0xFF0F2B66),
                                surfaceVariant = Color(0xFF1B3A6B),
                                outline = Color(0xFFF26729)
                        )
        ) {
                Box(
                        modifier = Modifier
                                .fillMaxSize()
//                                .background(
//                                        brush = Brush.verticalGradient(
//                                                colors = listOf(
//                                                        Color(0xFF0A1B40), // Darker blue
//                                                        Color(0xFF0F2B66), // Primary dark blue
//                                                        Color(0xFF1B3A6B), // Lighter blue
//                                                        Color(0xFF0F2B66)  // Primary dark blue
//                                                )
//                                        )
//                                )
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
                                                                connectivity = 100f
                                                                waterSupply = 100f
                                                                security = 100f
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
                                                                val difficultyLevel = when {
                                                                        totalHours <= 4 -> "🟢 Fácil"
                                                                        totalHours <= 8 -> "🟡 Moderado"
                                                                        totalHours <= 12 -> "🟠 Difícil"
                                                                        else -> "🔴 Extremo"
                                                                }
                                                                
                                                                addNotification(
                                                                        "🗓️ DÍA $currentDay COMENZADO",
                                                                        NotificationType.INFO,
                                                                        if (currentDay == 1 && solarPanels == 0)
                                                                                "⚡ ${totalHours} horas de apagones ($difficultyLevel). Sin paneles solares será un desafío. ¡Administra bien tu energía!"
                                                                        else if (currentDay == 1)
                                                                                "🌞 ${totalHours} horas de apagones ($difficultyLevel). Tu sistema solar te dará ventaja durante los cortes"
                                                                        else if (solarPanels > 0)
                                                                                "☀️ Día $currentDay con ${totalHours}h de apagones. Tu sistema solar te mantiene protegido"
                                                                        else
                                                                                "⚠️ Otro día difícil: ${totalHours}h sin electricidad. Mantén dispositivos esenciales encendidos"
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
                                                                
                                                                // Registrar configuración solar
                                                                GameStatsCollector.setSolarConfiguration(panels, batts, batts * 5000f)

                                                                currentDay = 1
                                                                gameState =
                                                                        GameState.BLACKOUT_CONFIG
                                                                timeOfDay = 6
                                                                comfortLevel = 100f
                                                                moneySpent = 0f
                                                                foodSpoilage = 0f
                                                                productivity = 100f
                                                                hygiene = 100f
                                                                connectivity = 100f
                                                                waterSupply = 100f
                                                                security = 100f

                                                                addNotification(
                                                                        "🎉 SISTEMA SOLAR INSTALADO",
                                                                        NotificationType.SUCCESS,
                                                                        "⚡ ${panels} paneles (${panels * 505}W) + ${batts} baterías (${batts * 5}kWh). ¡Ya tienes independencia energética!"
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

                                                                // Registrar configuración solar
                                                                GameStatsCollector.setSolarConfiguration(panels, batts, batts * 5000f)

                                                                // Volver al juego sin reiniciar el día
                                                                gameState = GameState.PLAYING

                                                                addNotification(
                                                                        "✅ SISTEMA SOLAR ACTUALIZADO",
                                                                        NotificationType.SUCCESS,
                                                                        "⚡ ${panels} paneles (${panels * 505}W) + ${batts} baterías (${batts * 5}kWh)"
                                                                )
                                                        }
                                                )
                                        GameState.FINISHED ->
                                                FinalSummaryScreen(
                                                        score = score,
                                                        averageComfort = sessionSummary.averageComfort
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
                                                        connectivity = connectivity,
                                                        waterSupply = waterSupply,
                                                        security = security,
                                                        moneySaved = moneySaved,
                                                        co2Saved = co2Saved,
                                                        score = score,
                                                        gameSpeed = gameSpeed,
                                                        onSpeedChange = { gameSpeed = it },
                                                        onToggleAppliance = { toggleAppliance(it) },
                                                        onConfigureClick = {
                                                                // Siempre permitir reconfigurar el sistema solar
                                                                gameState = GameState.CONFIGURING
                                                        },
                                                        onFinishClick = {
                                                                GameStatsCollector.endSession()
                                                                gameState = GameState.FINISHED
                                                        },
                                                        isFirstTime = currentDay == 1 && timeOfDay < 8
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

                                // Situations overlay
                                currentSituation?.let { situation ->
                                        SituationDialog(
                                                situation = situation,
                                                onAnswer = { option -> handleSituationAnswer(option) },
                                                onDismiss = {
                                                        currentSituation = null
                                                        isPausedBySituation = false // Reanudar el juego si se omite
                                                }
                                        )
                                }

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
