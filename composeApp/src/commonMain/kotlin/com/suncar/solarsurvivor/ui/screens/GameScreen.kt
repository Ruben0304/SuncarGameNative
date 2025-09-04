package com.suncar.solarsurvivor.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.Appliance
import com.suncar.solarsurvivor.data.EnergySource
import com.suncar.solarsurvivor.ui.components.SpeedControl
import com.suncar.solarsurvivor.util.IconWithColor
import com.suncar.solarsurvivor.ui.components.atom.*
import com.suncar.solarsurvivor.ui.components.molecules.*
import com.suncar.solarsurvivor.ui.components.organisms.*
import kotlin.collections.component1
import kotlin.collections.component2


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
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {
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