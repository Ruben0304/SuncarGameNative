package com.suncar.solarsurvivor.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.Appliance
import com.suncar.solarsurvivor.ui.components.RecommendationCard
import com.suncar.solarsurvivor.ui.components.atom.*
import com.suncar.solarsurvivor.ui.components.molecules.*
import com.suncar.solarsurvivor.ui.components.organisms.*
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