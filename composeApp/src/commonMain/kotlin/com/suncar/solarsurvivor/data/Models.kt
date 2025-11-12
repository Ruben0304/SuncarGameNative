package com.suncar.solarsurvivor.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


// Data Classes
data class Appliance(
    val name: String,
    val icon: ImageVector,
    val iconColor: Color = Color.Unspecified,
    var on: Boolean = false,
    val priority: Int,
    val consumption: Int,
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
    FINISHED,
    BLACKOUT_CONFIG,
    SOLAR_STUDY
}

enum class EnergySource {
    GRID,
    SOLAR,
    BATTERY,
    NONE
}

enum class SituationDifficulty {
    EASY,
    MEDIUM,
    HARD
}

data class SituationOption(
    val text: String,
    val isCorrect: Boolean,
    val explanation: String
)

data class Situation(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: SituationDifficulty,
    val options: List<SituationOption>,
    val points: Int
)

data class SituationResult(
    val situation: Situation,
    val selectedOption: SituationOption,
    val pointsEarned: Int
)

// Solar Kit from real Suncar offers
data class SolarKit(
    val id: String,
    val nombreMarca: String,
    val kW: Float,
    val precio: Int,
    val cantidadPaneles: Int,
    val capacidadBateriaKWh: Float,
    val kWPaneles: Float?
) {
    // Convert to game units
    fun toGameUnits(): Pair<Int, Int> {
        val panels = cantidadPaneles
        val batteries = (capacidadBateriaKWh / 5f).toInt().coerceAtLeast(0)
        return Pair(panels, batteries)
    }

    // Generate contextual description
    fun getDescription(): String {
        return when {
            kW <= 3f -> "Ideal para casas pequeñas. Mantiene esenciales durante apagones: refrigerador, luces y ventilador."
            kW <= 6f -> "Perfecto para casas medianas. Confort completo en apagones incluyendo AC y electrodomésticos."
            else -> "Sistema robusto para casas grandes. Independencia energética total, incluso en apagones prolongados."
        }
    }

    // Short label for UI
    fun getLabel(): String {
        return "$nombreMarca ${kW.toInt()}kW"
    }
}

// Predefined kits from Suncar offers
object SolarKits {
    val allKits = listOf(
        SolarKit(
            id = "greenheiss_3kw",
            nombreMarca = "Greenheiss",
            kW = 3.0f,
            precio = 5440,
            cantidadPaneles = 6,
            capacidadBateriaKWh = 4.8f,
            kWPaneles = 3.54f
        ),
        SolarKit(
            id = "greenheiss_5kw",
            nombreMarca = "Greenheiss",
            kW = 5.0f,
            precio = 9911,
            cantidadPaneles = 10,
            capacidadBateriaKWh = 4.0f,
            kWPaneles = 5.9f
        ),
        SolarKit(
            id = "huawei_10kw",
            nombreMarca = "Huawei",
            kW = 10.0f,
            precio = 29249,
            cantidadPaneles = 26,
            capacidadBateriaKWh = 21.0f,
            kWPaneles = 15.34f
        ),
        SolarKit(
            id = "must_3kw",
            nombreMarca = "Must",
            kW = 3.0f,
            precio = 3830,
            cantidadPaneles = 4,
            capacidadBateriaKWh = 7.0f,
            kWPaneles = 2.36f
        ),
        SolarKit(
            id = "must_6kw",
            nombreMarca = "Must",
            kW = 6.0f,
            precio = 7561,
            cantidadPaneles = 8,
            capacidadBateriaKWh = 14.3f,
            kWPaneles = 4.72f
        ),
        SolarKit(
            id = "felicity_2kw",
            nombreMarca = "Felicity Solar",
            kW = 2.0f,
            precio = 2352,
            cantidadPaneles = 2,
            capacidadBateriaKWh = 2.56f,
            kWPaneles = 1.18f
        ),
        SolarKit(
            id = "felicity_5kw_mid",
            nombreMarca = "Felicity Solar",
            kW = 5.0f,
            precio = 6504,
            cantidadPaneles = 8,
            capacidadBateriaKWh = 10.0f,
            kWPaneles = 4.72f
        ),
        SolarKit(
            id = "felicity_10kw",
            nombreMarca = "Felicity Solar",
            kW = 10.0f,
            precio = 7934,
            cantidadPaneles = 8,
            capacidadBateriaKWh = 15.0f,
            kWPaneles = 4.72f
        ),
        SolarKit(
            id = "felicity_3kw",
            nombreMarca = "Felicity Solar",
            kW = 3.0f,
            precio = 5044,
            cantidadPaneles = 5,
            capacidadBateriaKWh = 10.0f,
            kWPaneles = 2.95f
        )
    )

    // Get kits sorted by power
    fun getKitsByPower(): List<SolarKit> {
        return allKits.sortedBy { it.kW }
    }

    // Get recommended kit based on required power
    fun getRecommendedKit(requiredPowerW: Int): SolarKit? {
        val requiredKW = requiredPowerW / 1000f
        return allKits.filter { it.kWPaneles != null && it.kWPaneles >= requiredKW }
            .minByOrNull { it.precio }
    }
}
