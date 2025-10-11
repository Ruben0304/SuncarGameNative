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
