package com.suncar.solarsurvivor.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suncar.solarsurvivor.data.SolarKits
import com.suncar.solarsurvivor.util.IconWithColor
import com.suncar.solarsurvivor.util.getScreenConfiguration
import com.suncar.solarsurvivor.ui.components.organisms.HelperSection
import com.suncar.solarsurvivor.ui.components.organisms.KitCard


private enum class ConfigurationMode {
    MANUAL,
    KIT
}

@Composable
fun ConfigurationScreen(onApply: (Int, Int) -> Unit) {
    var panels by remember { mutableStateOf(4) }
    var batteries by remember { mutableStateOf(2) }
    var showHelper by remember { mutableStateOf(true) }
    var configurationMode by remember { mutableStateOf(ConfigurationMode.MANUAL) }
    var selectedKitId by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    val kits = remember { SolarKits.getKitsByPower() }
    val selectedKit = kits.firstOrNull { it.id == selectedKitId }
    val kitGameUnits = selectedKit?.toGameUnits() ?: (0 to 0)
    val kitPanels = kitGameUnits.first
    val kitBatteries = kitGameUnits.second

    val monthlyGeneration = panels * 505 * 5 * 30 / 1000
    val applyPanels = if (configurationMode == ConfigurationMode.MANUAL) panels else kitPanels
    val applyBatteries =
        if (configurationMode == ConfigurationMode.MANUAL) batteries else kitBatteries
    val applyEnabled =
        if (configurationMode == ConfigurationMode.MANUAL) panels > 0 || batteries > 0
        else selectedKit != null
    val buttonLabel =
        when {
            configurationMode == ConfigurationMode.MANUAL && panels == 0 && batteries == 0 ->
                "Selecciona al menos 1 panel"
            configurationMode == ConfigurationMode.KIT && selectedKit == null ->
                "Selecciona un kit para continuar"
            configurationMode == ConfigurationMode.KIT ->
                "Instalar Kit y Continuar"
            else -> "Instalar y Continuar"
        }

    val screenWidth = getScreenConfiguration().screenWidthDp
    val isCompact = screenWidth < 600.dp
    val horizontalPadding = when {
        screenWidth > 1200.dp -> 64.dp
        screenWidth > 900.dp -> 48.dp
        screenWidth > 600.dp -> 32.dp
        else -> 16.dp
    }
    val verticalPadding = if (isCompact) 16.dp else 32.dp
    val maxContentWidth = if (screenWidth > 900.dp) 720.dp else screenWidth - (horizontalPadding * 2)

    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = maxContentWidth)
                    .fillMaxWidth()
                    .verticalScroll(state = scrollState, enabled = true),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Text(
            text = "Configura Tu Sistema Solar",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700),
            textAlign = if (isCompact) TextAlign.Center else TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Diseña tu independencia energética",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier =
                Modifier
                    .padding(top = 8.dp, bottom = 24.dp)
                    .fillMaxWidth(),
            textAlign = if (isCompact) TextAlign.Center else TextAlign.Start
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

        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 24.dp))

        // Mode selector
        val chipSpacing = if (isCompact) 8.dp else 12.dp
        val chipArrangement =
            if (isCompact) Arrangement.spacedBy(chipSpacing) else Arrangement.spacedBy(chipSpacing)
        if (isCompact) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = chipArrangement
            ) {
                FilterChip(
                    selected = configurationMode == ConfigurationMode.MANUAL,
                    onClick = { configurationMode = ConfigurationMode.MANUAL },
                    label = { Text("Configuración Manual") },
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF2A2A3E),
                            labelColor = Color.White,
                            selectedContainerColor = Color(0xFF4CAF50),
                            selectedLabelColor = Color.White
                        )
                )
                FilterChip(
                    selected = configurationMode == ConfigurationMode.KIT,
                    onClick = { configurationMode = ConfigurationMode.KIT },
                    label = { Text("Kits Predefinidos") },
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF2A2A3E),
                            labelColor = Color.White,
                            selectedContainerColor = Color(0xFFFFD700),
                            selectedLabelColor = Color.Black
                        )
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = chipArrangement
            ) {
                FilterChip(
                    selected = configurationMode == ConfigurationMode.MANUAL,
                    onClick = { configurationMode = ConfigurationMode.MANUAL },
                    label = { Text("Configuración Manual") },
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF2A2A3E),
                            labelColor = Color.White,
                            selectedContainerColor = Color(0xFF4CAF50),
                            selectedLabelColor = Color.White
                        )
                )
                FilterChip(
                    selected = configurationMode == ConfigurationMode.KIT,
                    onClick = { configurationMode = ConfigurationMode.KIT },
                    label = { Text("Kits Predefinidos") },
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF2A2A3E),
                            labelColor = Color.White,
                            selectedContainerColor = Color(0xFFFFD700),
                            selectedLabelColor = Color.Black
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 24.dp))

        when (configurationMode) {
            ConfigurationMode.MANUAL -> {
                // Manual configuration
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))) {
                    Column(
                        modifier =
                            Modifier.padding(
                                horizontal = if (isCompact) 16.dp else 24.dp,
                                vertical = if (isCompact) 20.dp else 24.dp
                            )
                    ) {
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

                        if (isCompact) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Slider(
                                    value = panels.toFloat(),
                                    onValueChange = { panels = it.toInt() },
                                    valueRange = 0f..12f,
                                    colors =
                                        SliderDefaults.colors(
                                            thumbColor = Color(0xFFFFD700),
                                            activeTrackColor = Color(0xFFFFD700)
                                        )
                                )
                                Text(
                                    text = "$panels paneles",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
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
                        }

                        if (isCompact) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        } else {
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

                        if (isCompact) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Slider(
                                    value = batteries.toFloat(),
                                    onValueChange = { batteries = it.toInt() },
                                    valueRange = 0f..6f,
                                    colors =
                                        SliderDefaults.colors(
                                            thumbColor = Color(0xFF4CAF50),
                                            activeTrackColor = Color(0xFF4CAF50)
                                        )
                                )
                                Text(
                                    text = "$batteries baterías",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
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
                        }

                        if (isCompact) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        } else {
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
            }
            ConfigurationMode.KIT -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(if (isCompact) 12.dp else 16.dp)
                ) {
                    Text(
                        text = "Elige un kit con equipos reales Suncar para avanzar más rápido.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cada kit viene preparado con paneles, baterías y precio real para que entiendas su equivalente en el juego.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))

                    kits.forEachIndexed { index, kit ->
                        KitCard(
                            kit = kit,
                            selected = selectedKitId == kit.id,
                            onClick = {
                                selectedKitId =
                                    if (selectedKitId == kit.id) null else kit.id
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (index != kits.lastIndex) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    selectedKit?.let { kit ->
                        Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))
                        Card(
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = Color(0xFF1F3321)
                                ),
                            border = BorderStroke(1.dp, Color(0xFF4CAF50))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Tu selección en el juego",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text =
                                        "Paneles instalados: $kitPanels (${kit.cantidadPaneles} reales)",
                                    color = Color.White
                                )
                                Text(
                                    text =
                                        "Baterías en juego: $kitBatteries (${kit.capacidadBateriaKWh} kWh reales)",
                                    color = Color.White
                                )
                                Text(
                                    text =
                                        "Precio de referencia: $${kit.precio}",
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Investment Summary
//        Card(
//            colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
//            border = BorderStroke(1.dp, Color(0xFFFFD700))
//        ) {
//            Column(modifier = Modifier.padding(20.dp)) {
//                Text(
//                    text = "Resumen de Inversión",
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFFFFD700)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    SummaryItem("Inversión Total:", "$${investment}")
//                    SummaryItem("Ahorro Mensual:", "$${monthlySavings.toInt()}")
//                    SummaryItem("Retorno (ROI):", "${roi.toInt()} años")
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Surface(
//                    color = Color(0x334CAF50),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.padding(12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        IconWithColor(imageVector = Icons.Default.CreditCard, tint = Color(0xFF4CAF50), size = 20.dp)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text =
//                                "Financiamiento disponible: Desde $200/mes con planes flexibles",
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    }
//                }
//            }
//        }

        Spacer(modifier = Modifier.height(if (isCompact) 24.dp else 32.dp))

        Button(
            onClick = { onApply(applyPanels, applyBatteries) },
            modifier = Modifier.fillMaxWidth().height(if (isCompact) 52.dp else 56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            enabled = applyEnabled
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = buttonLabel,
                fontSize = 18.sp
            )
        }
        }
    }
}
