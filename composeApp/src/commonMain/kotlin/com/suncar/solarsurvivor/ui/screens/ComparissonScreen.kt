package com.suncar.solarsurvivor.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.ComparisonData
import com.suncar.solarsurvivor.ui.components.molecules.ComparisonMetric
import com.suncar.solarsurvivor.ui.components.*


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