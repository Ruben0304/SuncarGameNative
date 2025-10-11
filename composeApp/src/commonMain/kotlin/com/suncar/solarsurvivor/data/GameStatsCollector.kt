package com.suncar.solarsurvivor.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.collections.mutableMapOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Singleton que recolecta y almacena estadísticas de la sesión de juego
 * para generar gráficos y análisis posteriores
 */
object GameStatsCollector {
    
    // Configuración del sistema instalado
    var solarPanelsInstalled by mutableStateOf(0)
    var batteriesInstalled by mutableStateOf(0)
    var maxBatteryCapacity by mutableStateOf(0f)
    
    // Métricas temporales
    var gameSessionId by mutableStateOf("")
    @OptIn(ExperimentalTime::class)
    var gameStartTime by mutableStateOf(Clock.System.now().toEpochMilliseconds())
    var totalGameTimeMinutes by mutableStateOf(0L)
    var totalDaysPlayed by mutableStateOf(0)
    var totalHoursSimulated by mutableStateOf(0)
    
    // Estadísticas por hora (para gráficos temporales)
    val hourlyStats = mutableStateListOf<HourlyGameStats>()
    
    // Estadísticas por día
    val dailyStats = mutableStateListOf<DailyGameStats>()
    
    // Resumen de la sesión
    var sessionSummary = mutableStateOf(GameSessionSummary())
    
    // Eventos importantes durante la partida
    val gameEvents = mutableStateListOf<GameEvent>()
    
    // Estado actual de electrodomésticos (para análisis de patrones)
    val applianceUsagePattern = mutableMapOf<String, ApplianceUsageStats>()
    
    /**
     * Inicializa una nueva sesión de juego
     */
    @OptIn(ExperimentalTime::class)
    fun startNewSession(sessionId: String = Clock.System.now().toEpochMilliseconds().toString()) {
        gameSessionId = sessionId
        gameStartTime = Clock.System.now().toEpochMilliseconds()
        totalGameTimeMinutes = 0L
        totalDaysPlayed = 0
        totalHoursSimulated = 0
        
        hourlyStats.clear()
        dailyStats.clear()
        gameEvents.clear()
        applianceUsagePattern.clear()
        
        sessionSummary.value = GameSessionSummary()
        
        // Registrar inicio de sesión
        addGameEvent("SESSION_START", "Sesión de juego iniciada", mapOf(
            "sessionId" to sessionId,
            "timestamp" to gameStartTime
        ))
    }
    
    /**
     * Captura el estado actual del juego cada hora simulada
     */
    @OptIn(ExperimentalTime::class)
    fun captureHourlyStats(
        currentDay: Int,
        timeOfDay: Int,
        isBlackout: Boolean,
        energySource: EnergySource,
        solarGeneration: Int,
        batteryCharge: Float,
        comfortLevel: Float,
        appliances: Map<String, Appliance>,
        temperature: Float,
        connectivity: Float,
        waterSupply: Float,
        security: Float,
        foodSpoilage: Float,
        productivity: Float,
        hygiene: Float
    ) {
        val totalConsumption = appliances.values.sumOf { if (it.on) it.consumption else 0 }
        val activeAppliances = appliances.filter { it.value.on }.keys.toList()
        
        val stats = HourlyGameStats(
            day = currentDay,
            hour = timeOfDay,
            isBlackout = isBlackout,
            energySource = energySource,
            solarGeneration = solarGeneration,
            totalConsumption = totalConsumption,
            batteryChargePercent = (batteryCharge / maxBatteryCapacity * 100f).takeIf { maxBatteryCapacity > 0 } ?: 0f,
            comfortLevel = comfortLevel,
            temperature = temperature,
            connectivity = connectivity,
            waterSupply = waterSupply,
            security = security,
            foodSpoilage = foodSpoilage,
            productivity = productivity,
            hygiene = hygiene,
            activeAppliances = activeAppliances,
            totalActiveDevices = activeAppliances.size,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
        
        hourlyStats.add(stats)
        totalHoursSimulated++
        
        // Actualizar patrones de uso de electrodomésticos
        updateApplianceUsagePatterns(appliances)
        
        // Actualizar resumen de sesión
        updateSessionSummary(stats)
    }
    
    /**
     * Captura estadísticas al final de cada día
     */
    fun captureDayEndStats(
        day: Int,
        finalComfortLevel: Float,
        totalBlackoutHours: Int,
        averageTemperature: Float,
        minComfortLevel: Float,
        maxSolarGeneration: Int,
        totalEnergyConsumed: Int,
        criticalEventsCount: Int
    ) {
        val dayStats = DailyGameStats(
            day = day,
            finalComfortLevel = finalComfortLevel,
            totalBlackoutHours = totalBlackoutHours,
            averageTemperature = averageTemperature,
            minComfortLevel = minComfortLevel,
            maxSolarGeneration = maxSolarGeneration,
            totalEnergyConsumed = totalEnergyConsumed,
            criticalEventsCount = criticalEventsCount,
            hoursPlayed = hourlyStats.count { it.day == day }
        )
        
        dailyStats.add(dayStats)
        totalDaysPlayed++
    }
    
    /**
     * Registra eventos importantes del juego
     */
    @OptIn(ExperimentalTime::class)
    fun addGameEvent(
        type: String,
        description: String,
        metadata: Map<String, Any> = emptyMap()
    ) {
        gameEvents.add(
            GameEvent(
                type = type,
                description = description,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                day = totalDaysPlayed,
                hour = totalHoursSimulated % 24,
                metadata = metadata
            )
        )
    }
    
    /**
     * Registra la configuración del sistema solar
     */
    fun setSolarConfiguration(panels: Int, batteries: Int, maxCapacity: Float) {
        solarPanelsInstalled = panels
        batteriesInstalled = batteries
        maxBatteryCapacity = maxCapacity
        
        addGameEvent("SOLAR_CONFIG", "Sistema solar configurado", mapOf(
            "panels" to panels,
            "batteries" to batteries,
            "maxCapacity" to maxCapacity
        ))
    }
    
    /**
     * Finaliza la sesión y calcula estadísticas finales
     */
    @OptIn(ExperimentalTime::class)
    fun endSession() {
        totalGameTimeMinutes = (Clock.System.now().toEpochMilliseconds() - gameStartTime) / 60000
        
        addGameEvent("SESSION_END", "Sesión de juego finalizada", mapOf(
            "totalGameTimeMinutes" to totalGameTimeMinutes,
            "totalDaysPlayed" to totalDaysPlayed,
            "totalHoursSimulated" to totalHoursSimulated
        ))
        
        // Calcular estadísticas finales
        calculateFinalSessionSummary()
    }
    
    /**
     * Obtiene estadísticas para gráficos
     */
    fun getChartData(): GameChartData {
        return GameChartData(
            comfortOverTime = hourlyStats.map { ChartPoint(it.hour.toFloat(), it.comfortLevel) },
            energyGenerationOverTime = hourlyStats.map { ChartPoint(it.hour.toFloat(), it.solarGeneration.toFloat()) },
            energyConsumptionOverTime = hourlyStats.map { ChartPoint(it.hour.toFloat(), it.totalConsumption.toFloat()) },
            temperatureOverTime = hourlyStats.map { ChartPoint(it.hour.toFloat(), it.temperature) },
            batteryLevelOverTime = hourlyStats.map { ChartPoint(it.hour.toFloat(), it.batteryChargePercent) },
            blackoutPeriods = hourlyStats.filter { it.isBlackout }.map { it.hour.toFloat() },
            dailyComfortAverage = dailyStats.map { ChartPoint(it.day.toFloat(), it.finalComfortLevel) },
            applianceUsageStats = applianceUsagePattern.toMap()
        )
    }
    
    private fun updateApplianceUsagePatterns(appliances: Map<String, Appliance>) {
        appliances.forEach { (key, appliance) ->
            val currentStats = applianceUsagePattern.getOrPut(key) {
                ApplianceUsageStats(
                    applianceName = appliance.name,
                    totalOnTime = 0,
                    totalConsumption = 0f,
                    timesToggled = 0,
                    lastState = false
                )
            }
            
            // Si cambió de estado, incrementar contador
            if (currentStats.lastState != appliance.on) {
                applianceUsagePattern[key] = currentStats.copy(
                    timesToggled = currentStats.timesToggled + 1,
                    lastState = appliance.on
                )
            }
            
            // Si está encendido, incrementar tiempo y consumo
            if (appliance.on) {
                applianceUsagePattern[key] = applianceUsagePattern[key]!!.copy(
                    totalOnTime = currentStats.totalOnTime + 1,
                    totalConsumption = currentStats.totalConsumption + appliance.consumption
                )
            }
        }
    }
    
    private fun updateSessionSummary(stats: HourlyGameStats) {
        val current = sessionSummary.value
        sessionSummary.value = current.copy(
            totalSolarGenerated = current.totalSolarGenerated + stats.solarGeneration,
            totalEnergyConsumed = current.totalEnergyConsumed + stats.totalConsumption,
            averageComfort = ((current.averageComfort * (totalHoursSimulated - 1)) + stats.comfortLevel) / totalHoursSimulated,
            minComfort = minOf(current.minComfort, stats.comfortLevel),
            maxComfort = maxOf(current.maxComfort, stats.comfortLevel),
            totalBlackoutHours = current.totalBlackoutHours + if (stats.isBlackout) 1 else 0,
            maxTemperature = maxOf(current.maxTemperature, stats.temperature),
            minTemperature = minOf(current.minTemperature, stats.temperature)
        )
    }
    
    private fun calculateFinalSessionSummary() {
        val current = sessionSummary.value
        
        sessionSummary.value = current.copy(
            gameEfficiencyScore = calculateEfficiencyScore(),
            totalGameTimeMinutes = totalGameTimeMinutes
        )
    }
    
    private fun calculateEfficiencyScore(): Float {
        if (hourlyStats.isEmpty()) return 0f
        
        val avgComfort = sessionSummary.value.averageComfort
        val energyEfficiency = if (sessionSummary.value.totalEnergyConsumed > 0) {
            sessionSummary.value.totalSolarGenerated / sessionSummary.value.totalEnergyConsumed.toFloat()
        } else 0f
        
        val blackoutSurvival = 1f - (sessionSummary.value.totalBlackoutHours / totalHoursSimulated.toFloat())
        
        return (avgComfort * 0.4f + energyEfficiency * 30f + blackoutSurvival * 30f)
            .coerceIn(0f, 100f)
    }
}

// Data classes para estadísticas
data class HourlyGameStats(
    val day: Int,
    val hour: Int,
    val isBlackout: Boolean,
    val energySource: EnergySource,
    val solarGeneration: Int,
    val totalConsumption: Int,
    val batteryChargePercent: Float,
    val comfortLevel: Float,
    val temperature: Float,
    val connectivity: Float,
    val waterSupply: Float,
    val security: Float,
    val foodSpoilage: Float,
    val productivity: Float,
    val hygiene: Float,
    val activeAppliances: List<String>,
    val totalActiveDevices: Int,
    val timestamp: Long
)

data class DailyGameStats(
    val day: Int,
    val finalComfortLevel: Float,
    val totalBlackoutHours: Int,
    val averageTemperature: Float,
    val minComfortLevel: Float,
    val maxSolarGeneration: Int,
    val totalEnergyConsumed: Int,
    val criticalEventsCount: Int,
    val hoursPlayed: Int
)

data class GameEvent(
    val type: String,
    val description: String,
    val timestamp: Long,
    val day: Int,
    val hour: Int,
    val metadata: Map<String, Any>
)

data class GameSessionSummary(
    val totalSolarGenerated: Int = 0,
    val totalEnergyConsumed: Int = 0,
    val averageComfort: Float = 100f,
    val minComfort: Float = 100f,
    val maxComfort: Float = 100f,
    val totalBlackoutHours: Int = 0,
    val maxTemperature: Float = 0f,
    val minTemperature: Float = 50f,
    val gameEfficiencyScore: Float = 0f,
    val totalGameTimeMinutes: Long = 0L
)

data class ApplianceUsageStats(
    val applianceName: String,
    val totalOnTime: Int,
    val totalConsumption: Float,
    val timesToggled: Int,
    val lastState: Boolean
)

data class ChartPoint(
    val x: Float,
    val y: Float
)

data class GameChartData(
    val comfortOverTime: List<ChartPoint>,
    val energyGenerationOverTime: List<ChartPoint>,
    val energyConsumptionOverTime: List<ChartPoint>,
    val temperatureOverTime: List<ChartPoint>,
    val batteryLevelOverTime: List<ChartPoint>,
    val blackoutPeriods: List<Float>,
    val dailyComfortAverage: List<ChartPoint>,
    val applianceUsageStats: Map<String, ApplianceUsageStats>
)