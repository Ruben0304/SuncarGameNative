package com.suncar.solarsurvivor.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.suncar.solarsurvivor.data.Appliance
import com.suncar.solarsurvivor.data.SolarKits
import com.suncar.solarsurvivor.ui.components.RecommendationCard
import com.suncar.solarsurvivor.ui.components.atom.*
import com.suncar.solarsurvivor.ui.components.molecules.*
import com.suncar.solarsurvivor.ui.components.organisms.*
import com.suncar.solarsurvivor.ui.components.organisms.ManualCustomizationModal
import com.suncar.solarsurvivor.util.getScreenConfiguration
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.math.ceil


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
    var showManualModal by remember { mutableStateOf(false) }
    var showKitOptions by remember { mutableStateOf(false) }
    var selectedKitId by remember { mutableStateOf<String?>(null) }

    val kits = remember { SolarKits.getKitsByPower() }
    val selectedKit = kits.firstOrNull { it.id == selectedKitId }
    val kitGameUnits = selectedKit?.toGameUnits() ?: (0 to 0)
    val kitPanels = kitGameUnits.first
    val kitBatteries = kitGameUnits.second

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
    val headlineStyle =
        if (isCompact) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineMedium
    val sectionTitleStyle =
        if (isCompact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge
    val primaryBodyStyle =
        if (isCompact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge
    val secondaryBodyStyle =
        if (isCompact) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium
    val accentTitleStyle =
        if (isCompact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium
    val contentModifier =
        Modifier
            .fillMaxWidth()
            .widthIn(max = maxContentWidth)

    LazyColumn(
        modifier =
            Modifier.fillMaxSize()
                .background(Color.Black),
        contentPadding =
            PaddingValues(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            if (isCompact) {
                Column(
                    modifier = contentModifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Estudio Solar Personalizado",
                        style = headlineStyle,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Row(
                    modifier = contentModifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Estudio Solar Personalizado",
                        style = headlineStyle,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            Text(
                text = "Diseñaremos el sistema perfecto para tus necesidades",
                style = primaryBodyStyle,
                color = Color.White.copy(alpha = 0.9f),
                modifier =
                    contentModifier.padding(top = 8.dp, bottom = 32.dp),
                textAlign = if (isCompact) TextAlign.Center else TextAlign.Start
            )
        }

        // Section 1: Household Info
        item {
            Card(
                modifier = contentModifier,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
            ) {
                Column(
                    modifier =
                        Modifier.padding(
                            horizontal = if (isCompact) 16.dp else 24.dp,
                            vertical = if (isCompact) 20.dp else 24.dp
                        )
                ) {
                    Text(
                        text = "1. Información del Hogar",
                        style = sectionTitleStyle,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )

                    Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))

                    Text(
                        text = "Personas en el hogar:",
                        color = Color.White.copy(alpha = 0.9f),
                        style = primaryBodyStyle
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

                    Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))

                    Text(
                        text = "Presupuesto disponible:",
                        color = Color.White.copy(alpha = 0.9f),
                        style = primaryBodyStyle
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isCompact) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            BudgetOption(
                                title = "Básico",
                                range = "$2,000-4,000",
                                selected = budget == "low",
                                onClick = { budget = "low" },
                                modifier = Modifier.fillMaxWidth()
                            )
                            BudgetOption(
                                title = "Estándar",
                                range = "$4,000-8,000",
                                selected = budget == "medium",
                                onClick = { budget = "medium" },
                                modifier = Modifier.fillMaxWidth()
                            )
                            BudgetOption(
                                title = "Premium",
                                range = "$8,000+",
                                selected = budget == "high",
                                onClick = { budget = "high" },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
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
            }

            Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 24.dp))
        }

        // Section 2: Priorities
        item {
            Card(
                modifier = contentModifier,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
            ) {
                Column(
                    modifier =
                        Modifier.padding(
                            horizontal = if (isCompact) 16.dp else 24.dp,
                            vertical = if (isCompact) 20.dp else 24.dp
                        )
                ) {
                    Text(
                        text =
                            "2. ¿Qué necesitas mantener encendido durante apagones?",
                        style = sectionTitleStyle,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )

                    Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))

                    val priorityColumns =
                        if (isCompact) GridCells.Fixed(2) else GridCells.Adaptive(150.dp)
                    LazyVerticalGrid(
                        columns = priorityColumns,
                        modifier =
                            Modifier.fillMaxWidth()
                                .heightIn(
                                    min = if (isCompact) 260.dp else 320.dp,
                                    max = if (isCompact) 360.dp else 420.dp
                                ),
                        verticalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp)
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

                    Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))

                    Surface(
                        color = Color(0x33FFD700),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text =
                                "Consumo total seleccionado: ${requiredPower}W",
                            modifier = Modifier.padding(16.dp),
                            style = primaryBodyStyle,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 24.dp))
        }

        // Section 3: Recommendation
        item {
            Card(
                modifier = contentModifier,
                colors =
                    CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                border = BorderStroke(2.dp, Color(0xFFFFD700))
            ) {
                Column(
                    modifier =
                        Modifier.padding(
                            horizontal = if (isCompact) 16.dp else 24.dp,
                            vertical = if (isCompact) 20.dp else 24.dp
                        )
                ) {
                    Text(
                        text = "3. Sistema Recomendado para Ti",
                        style = sectionTitleStyle,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )

                    Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 24.dp))

                    if (isCompact) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RecommendationCard(
                                icon = Icons.Default.WbSunny,
                                value = panels.toString(),
                                label = "Paneles Solares",
                                detail = "505W c/u",
                                color = Color(0xFFFFD700),
                                modifier = Modifier.fillMaxWidth()
                            )
                            RecommendationCard(
                                icon = Icons.Default.Battery1Bar,
                                value = batteries.toString(),
                                label = "Baterías",
                                detail = "5kWh c/u",
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.fillMaxWidth()
                            )
                            RecommendationCard(
                                icon = Icons.Default.AttachMoney,
                                value = "$${investment}",
                                label = "Inversión Total",
                                detail = "Financiamiento disponible",
                                color = Color(0xFF2196F3),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
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
                    }

                    Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 24.dp))

                    Card(
                        colors =
                            CardDefaults.cardColors(
                                containerColor = Color(0x334CAF50)
                            )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Con este sistema podrás:",
                                style = accentTitleStyle,
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

            Spacer(modifier = Modifier.height(if (isCompact) 24.dp else 32.dp))
        }

        // Actions
        item {
            Column(modifier = contentModifier) {
                if (isCompact) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showManualModal = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                )
                        ) { Text("Personalizar Manualmente") }
                        Button(
                            onClick = { onComplete(panels, batteries) },
                            modifier = Modifier.fillMaxWidth(),
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
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showManualModal = true },
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

                Spacer(modifier = Modifier.height(if (isCompact) 12.dp else 16.dp))

                OutlinedButton(
                    onClick = {
                        showKitOptions = !showKitOptions
                        if (!showKitOptions) {
                            selectedKitId = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                ) {
                    Text(
                        text =
                            if (showKitOptions) "Ocultar Kits Predefinidos"
                            else "Ver Kits Predefinidos"
                    )
                }

                if (showKitOptions) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Kits Suncar recomendados",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Explora las ofertas reales y tradúcelas a tu partida con un clic.",
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
                            Spacer(modifier = Modifier.height(if (isCompact) 12.dp else 16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))

                    selectedKit?.let { kit ->
                        Card(
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = Color(0xFF1F3321)
                                ),
                            border = BorderStroke(1.dp, Color(0xFF4CAF50))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Tu selección se traduce a:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "$kitPanels paneles dentro del juego",
                                    color = Color.White
                                )
                                Text(
                                    text =
                                        "$kitBatteries baterías de 5kWh c/u (${kit.capacidadBateriaKWh} kWh reales)",
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(if (isCompact) 12.dp else 16.dp))
                    } ?: run {
                        Text(
                            text = "Selecciona uno de los kits para continuar.",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(if (isCompact) 12.dp else 16.dp))
                    }

                    Button(
                        onClick = {
                            selectedKit?.let {
                                onComplete(kitPanels, kitBatteries)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(if (isCompact) 52.dp else 56.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                        enabled = selectedKit != null
                    ) {
                        Text(
                            text =
                                selectedKit?.let { "Instalar ${it.getLabel()}" }
                                    ?: "Selecciona un kit",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
    
    // Manual customization modal
    if (showManualModal) {
        ManualCustomizationModal(
            initialPanels = panels,
            initialBatteries = batteries,
            onApply = { newPanels, newBatteries ->
                onComplete(newPanels, newBatteries)
                showManualModal = false
            },
            onDismiss = { showManualModal = false }
        )
    }
}
