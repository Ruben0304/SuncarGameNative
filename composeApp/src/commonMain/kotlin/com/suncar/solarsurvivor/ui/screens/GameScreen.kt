package com.suncar.solarsurvivor.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.Appliance
import com.suncar.solarsurvivor.data.EnergySource
import com.suncar.solarsurvivor.ui.components.SpeedControl
import com.suncar.solarsurvivor.util.IconWithColor
import com.suncar.solarsurvivor.ui.components.atom.*
import com.suncar.solarsurvivor.ui.components.molecules.*
import com.suncar.solarsurvivor.ui.components.organisms.*
import com.suncar.solarsurvivor.ui.components.molecules.HelpTooltip
import com.suncar.solarsurvivor.ui.components.organisms.FirstTimeGameHelper
import com.suncar.solarsurvivor.ui.components.organisms.EnergySourceHelper
import com.suncar.solarsurvivor.ui.components.organisms.ComfortLevelHelper
import com.suncar.solarsurvivor.ui.components.organisms.PowerBalanceHelper
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
    connectivity: Float,
    waterSupply: Float,
    security: Float,
    moneySaved: Float,
    co2Saved: Float,
    score: Int,
    gameSpeed: Long,
    onSpeedChange: (Long) -> Unit,
    onToggleAppliance: (String) -> Unit,
    onConfigureClick: () -> Unit,
    onFinishClick: () -> Unit,
    isFirstTime: Boolean = currentDay == 1 && timeOfDay < 8
) {
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val isCompact = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isCompact) 8.dp else 16.dp)
                .verticalScroll(scrollState)
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
            ) {
                val timeSection: @Composable () -> Unit = {
                    Column {
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

                        Spacer(modifier = Modifier.height(8.dp))

                        // Score Display
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFFFDB813)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Puntuación: $score pts",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFDB813)
                            )
                        }
                    }
                }

                val energySection: @Composable () -> Unit = {
                    if (isCompact) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
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
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SpeedControl(gameSpeed, onSpeedChange)
                                HelpTooltip(
                                    text = "Controla la velocidad del tiempo. Acelera para pasar rápido por las horas, ralentiza para gestionar mejor tu energía.",
                                    icon = Icons.Default.HelpOutline,
                                    iconColor = Color(0xFFFFD700)
                                )
                            }
                        }
                    } else {
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

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SpeedControl(gameSpeed, onSpeedChange)
                                HelpTooltip(
                                    text = "Controla la velocidad del tiempo. Acelera para pasar rápido por las horas, ralentiza para gestionar mejor tu energía.",
                                    icon = Icons.Default.HelpOutline,
                                    iconColor = Color(0xFFFFD700)
                                )
                            }
                        }
                    }
                }

                val metricsSection: @Composable () -> Unit = {
                    if (isCompact) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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
                            }
                            LinearProgressIndicator(
                                progress = comfortLevel / 100f,
                                modifier = Modifier.fillMaxWidth(),
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
                    } else {
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

                if (isCompact) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        timeSection()
                        energySection()
                        metricsSection()
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        timeSection()
                        energySection()
                        metricsSection()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Helper de primera vez
            FirstTimeGameHelper(
                isFirstTime = isFirstTime,
                currentDay = currentDay,
                modifier = Modifier.fillMaxWidth(),
                onDismiss = { /* El usuario puede cerrar este helper */ }
            )

            if (isFirstTime) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isCompact) {
                // Layout en columna para pantallas pequeñas
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Panel lateral primero en móvil
                    SidePanel(
                        energySource = energySource,
                        isBlackout = isBlackout,
                        solarPanels = solarPanels,
                        currentSolarGeneration = currentSolarGeneration,
                        batteries = batteries,
                        batteryCharge = batteryCharge,
                        maxBatteryCapacity = maxBatteryCapacity,
                        appliances = appliances,
                        comfortLevel = comfortLevel,
                        foodSpoilage = foodSpoilage,
                        productivity = productivity,
                        hygiene = hygiene,
                        temperature = temperature,
                        connectivity = connectivity,
                        waterSupply = waterSupply,
                        security = security,
                        currentDay = currentDay,
                        onConfigureClick = onConfigureClick,
                        onFinishClick = onFinishClick,
                        isCompact = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Área principal de la casa
                    MainHouseArea(
                        solarPanels = solarPanels,
                        currentSolarGeneration = currentSolarGeneration,
                        appliances = appliances,
                        isBlackout = isBlackout,
                        batteryCharge = batteryCharge,
                        onToggleAppliance = onToggleAppliance,
                        isCompact = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                // Layout en fila para pantallas grandes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Área principal de la casa
                    MainHouseArea(
                        solarPanels = solarPanels,
                        currentSolarGeneration = currentSolarGeneration,
                        appliances = appliances,
                        isBlackout = isBlackout,
                        batteryCharge = batteryCharge,
                        onToggleAppliance = onToggleAppliance,
                        isCompact = false,
                        modifier = Modifier.weight(1f)
                    )

                    // Panel lateral
                    SidePanel(
                        energySource = energySource,
                        isBlackout = isBlackout,
                        solarPanels = solarPanels,
                        currentSolarGeneration = currentSolarGeneration,
                        batteries = batteries,
                        batteryCharge = batteryCharge,
                        maxBatteryCapacity = maxBatteryCapacity,
                        appliances = appliances,
                        comfortLevel = comfortLevel,
                        foodSpoilage = foodSpoilage,
                        productivity = productivity,
                        hygiene = hygiene,
                        temperature = temperature,
                        connectivity = connectivity,
                        waterSupply = waterSupply,
                        security = security,
                        currentDay = currentDay,
                        onConfigureClick = onConfigureClick,
                        onFinishClick = onFinishClick,
                        isCompact = false,
                        modifier = Modifier.width(350.dp)
                    )
                }
            }
        }
    }


}

@Composable
fun MainHouseArea(
    solarPanels: Int,
    currentSolarGeneration: Int,
    appliances: Map<String, Appliance>,
    isBlackout: Boolean,
    batteryCharge: Float,
    onToggleAppliance: (String) -> Unit,
    isCompact: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
                        IconWithColor(
                            imageVector = Icons.Default.WbSunny,
                            tint = Color(0xFFFFD700),
                            size = 24.dp
                        )
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
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A2A3E)
            )
        ) {
            LazyVerticalGrid(
                columns = if (isCompact) GridCells.Fixed(2) else GridCells.Adaptive(150.dp),
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(16.dp)
                        .heightIn(
                            min = 240.dp,
                            max = if (isCompact) 420.dp else 480.dp
                        ),
                verticalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp),
                horizontalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp)
            ) {
                items(appliances.entries.toList()) { (key, appliance) ->
                    val totalConsumption =
                        appliances.values.sumOf { if (it.on) it.consumption else 0 }
                    val availableEnergy = currentSolarGeneration +
                            (if (isBlackout) 0 else 2000) +
                            (if (batteryCharge > 0) 1000 else 0)
                    val energyAvailable =
                        appliance.on || (totalConsumption + appliance.consumption <= availableEnergy)

                    ApplianceCard(
                        appliance = appliance,
                        onClick = { onToggleAppliance(key) },
                        isEnergyAvailable = energyAvailable
                    )
                }
            }
        }
    }

}
@Composable
fun SidePanel(
    energySource: EnergySource,
    isBlackout: Boolean,
    solarPanels: Int,
    currentSolarGeneration: Int,
    batteries: Int,
    batteryCharge: Float,
    maxBatteryCapacity: Float,
    appliances: Map<String, Appliance>,
    comfortLevel: Float,
    foodSpoilage: Float,
    productivity: Float,
    hygiene: Float,
    temperature: Float,
    connectivity: Float,
    waterSupply: Float,
    security: Float,
    currentDay: Int,
    onConfigureClick: () -> Unit,
    onFinishClick: () -> Unit,
    isCompact: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 12.dp else 16.dp)
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

                Spacer(modifier = Modifier.height(8.dp))

                // Helper de fuente de energía
                EnergySourceHelper(
                    energySource = energySource,
                    isBlackout = isBlackout,
                    solarPanels = solarPanels,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowItem(
                    Icons.Default.WbSunny,
                    "Solar: ${currentSolarGeneration}W"
                )

                if (batteries > 0) {
                    FlowItem(
                        Icons.Default.Battery1Bar,
                        "Batería: ${(batteryCharge / 1000).toInt()}/${(maxBatteryCapacity / 1000).toInt()}kWh"
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

                Spacer(modifier = Modifier.height(8.dp))

                // Helper de balance de energía
                PowerBalanceHelper(
                    solarGeneration = currentSolarGeneration,
                    totalConsumption = totalConsumption,
                    batteryCharge = batteryCharge,
                    maxBatteryCapacity = maxBatteryCapacity,
                    isBlackout = isBlackout,
                    modifier = Modifier.fillMaxWidth()
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

                Spacer(modifier = Modifier.height(8.dp))

                // Helper contextual de comfort
                ComfortLevelHelper(
                    comfortLevel = comfortLevel,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Temperatura mejorada con ícono de calor
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = if (isCompact) Modifier.weight(1f) else Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Thermostat,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = when {
                                temperature > 32 -> Color(0xFFFF6B6B)
                                temperature > 28 -> Color(0xFFFFA500) 
                                else -> Color(0xFF4CAF50)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calor:", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(if (isCompact) 8.dp else 12.dp))
                    Surface(
                        modifier = if (isCompact) Modifier.weight(1f, fill = false) else Modifier,
                        color = when {
                            temperature > 32 -> Color(0x33FF6B6B)
                            temperature > 28 -> Color(0x33FFA500)
                            else -> Color(0x334CAF50)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = when {
                                temperature > 35 -> "🔥 Extremo (${temperature.toInt()}°C)"
                                temperature > 30 -> "🌡️ Caluroso (${temperature.toInt()}°C)"
                                temperature > 26 -> "😌 Cómodo (${temperature.toInt()}°C)"
                                else -> "❄️ Fresco (${temperature.toInt()}°C)"
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold,
                            color = when {
                                temperature > 32 -> Color(0xFFFF6B6B)
                                temperature > 28 -> Color(0xFFFFA500)
                                else -> Color(0xFF4CAF50)
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Indicadores vitales durante apagones
                MetricItem("🍎 Alimentos:", 100 - foodSpoilage)
                MetricItem("💼 Productividad:", productivity)
                MetricItem("🚿 Higiene:", hygiene)
                
                // Nuevos indicadores vitales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCompact) Arrangement.spacedBy(8.dp) else Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "📡 Conectividad:",
                        color = Color.White,
                        modifier = if (isCompact) Modifier.weight(1f) else Modifier
                    )
                    Text(
                        text = when {
                            connectivity >= 80 -> "🟢 Excelente (${connectivity.toInt()}%)"
                            connectivity >= 40 -> "🟡 Básica (${connectivity.toInt()}%)"
                            else -> "🔴 Sin conexión (${connectivity.toInt()}%)"
                        },
                        color = when {
                            connectivity >= 80 -> Color(0xFF4CAF50)
                            connectivity >= 40 -> Color(0xFFFFA500)
                            else -> Color(0xFFFF6B6B)
                        },
                        fontWeight = FontWeight.Bold,
                        modifier = if (isCompact) Modifier.weight(1f) else Modifier,
                        textAlign = if (isCompact) TextAlign.End else TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(if (isCompact) 6.dp else 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCompact) Arrangement.spacedBy(8.dp) else Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "💧 Agua Corriente:",
                        color = Color.White,
                        modifier = if (isCompact) Modifier.weight(1f) else Modifier
                    )
                    Text(
                        text = when {
                            waterSupply >= 100 -> "🟢 Disponible"
                            waterSupply >= 50 -> "🟡 Limitada"
                            else -> "🔴 Sin servicio"
                        },
                        color = when {
                            waterSupply >= 100 -> Color(0xFF4CAF50)
                            waterSupply >= 50 -> Color(0xFFFFA500)
                            else -> Color(0xFFFF6B6B)
                        },
                        fontWeight = FontWeight.Bold,
                        modifier = if (isCompact) Modifier.weight(1f) else Modifier,
                        textAlign = if (isCompact) TextAlign.End else TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(if (isCompact) 6.dp else 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCompact) Arrangement.spacedBy(8.dp) else Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🔒 Seguridad:",
                        color = Color.White,
                        modifier = if (isCompact) Modifier.weight(1f) else Modifier
                    )
                    Text(
                        text = when {
                            security >= 80 -> "🟢 Protegido (${security.toInt()}%)"
                            security >= 50 -> "🟡 Básico (${security.toInt()}%)"
                            else -> "🔴 Vulnerable (${security.toInt()}%)"
                        },
                        color = when {
                            security >= 80 -> Color(0xFF4CAF50)
                            security >= 50 -> Color(0xFFFFA500)
                            else -> Color(0xFFFF6B6B)
                        },
                        fontWeight = FontWeight.Bold,
                        modifier = if (isCompact) Modifier.weight(1f) else Modifier,
                        textAlign = if (isCompact) TextAlign.End else TextAlign.Start
                    )
                }
            }
        }


        // Botón de configuración solar - siempre disponible
        Button(
            onClick = onConfigureClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700)
            )
        ) {
            Text(
                text = "Configurar Sistema Solar",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(if (isCompact) 8.dp else 12.dp))

        Button(
            onClick = onFinishClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text(
                text = "Terminar",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
