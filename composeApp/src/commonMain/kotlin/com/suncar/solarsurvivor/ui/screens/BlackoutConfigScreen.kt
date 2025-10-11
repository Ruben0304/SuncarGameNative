package com.suncar.solarsurvivor.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.remember
import com.suncar.solarsurvivor.util.getScreenConfiguration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.sp
import com.suncar.solarsurvivor.data.BlackoutSchedule
import com.suncar.solarsurvivor.ui.components.*
import com.suncar.solarsurvivor.ui.components.organisms.ImpactStat


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlackoutConfigScreen(day: Int, onConfirm: (List<BlackoutSchedule>) -> Unit) {
    var schedules by remember { mutableStateOf(listOf(BlackoutSchedule(10, 14))) }

    val scrollState = rememberScrollState()
    val screenConfiguration = getScreenConfiguration()
    val screenWidth = screenConfiguration.screenWidthDp
    
    // Responsive padding and max width
    val horizontalPadding = when {
        screenWidth > 1200.dp -> 64.dp
        screenWidth > 800.dp -> 48.dp
        else -> 24.dp
    }
    
    val maxContentWidth = when {
        screenWidth > 1200.dp -> 800.dp
        screenWidth > 800.dp -> 700.dp
        else -> screenWidth - (horizontalPadding * 2)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxContentWidth)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFFFFD700)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Programa de Apagones - Día $day",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
        }

        Text(
            text = "Configura los horarios de cortes eléctricos para hoy",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // Quick suggestions - Responsive flow layout
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    schedules = listOf(BlackoutSchedule(10, 14))
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
            ) { 
                Text(
                    "Bajo 4h",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            OutlinedButton(
                onClick = {
                    schedules = listOf(
                        BlackoutSchedule(10, 14),
                        BlackoutSchedule(18, 24)
                    )
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
            ) { 
                Text(
                    "Medio 10h",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            OutlinedButton(
                onClick = {
                    schedules = listOf(
                        BlackoutSchedule(2, 5),
                        BlackoutSchedule(7, 14),
                        BlackoutSchedule(16, 24)
                    )
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF6B6B)),
                border = BorderStroke(1.dp, Color(0xFFFF6B6B))
            ) { 
                Text(
                    "Zona Crítica (18h)",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Schedules
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                schedules.forEachIndexed { index, schedule ->
                    ScheduleRow(
                        index = index + 1,
                        schedule = schedule,
                        onStartChange = { newStart ->
                            schedules =
                                schedules.toMutableList().apply {
                                    this[index] =
                                        schedule.copy(
                                            start =
                                                newStart
                                        )
                                }
                        },
                        onEndChange = { newEnd ->
                            schedules =
                                schedules.toMutableList().apply {
                                    this[index] =
                                        schedule.copy(
                                            end = newEnd
                                        )
                                }
                        },
                        onRemove =
                            if (schedules.size > 1) {
                                {
                                    schedules =
                                        schedules
                                            .filterIndexed {
                                                    i,
                                                    _ ->
                                                i !=
                                                        index
                                            }
                                }
                            } else null
                    )
                    if (index < schedules.size - 1) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (schedules.size < 4) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            val lastEnd =
                                schedules.lastOrNull()?.end ?: 8
                            schedules =
                                schedules +
                                        BlackoutSchedule(
                                            minOf(
                                                lastEnd + 2,
                                                22
                                            ),
                                            minOf(
                                                lastEnd + 4,
                                                23
                                            )
                                        )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF4CAF50)
                            ),
                        border = BorderStroke(2.dp, Color(0xFF4CAF50))
                    ) { Text("+ Agregar otro apagón") }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Impact preview - Responsive grid
        val totalHours = schedules.sumOf { it.end - it.start }
        val gridColumns = when {
            screenWidth > 800.dp -> 3
            screenWidth > 500.dp -> 2
            else -> 1
        }
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns),
            modifier = Modifier.fillMaxWidth().height(
                when (gridColumns) {
                    1 -> 300.dp
                    2 -> 200.dp
                    else -> 120.dp
                }
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ImpactStat(
                    icon = Icons.Default.Warning,
                    value = "$totalHours horas",
                    label = "sin electricidad",
                    severity = when {
                        totalHours > 12 -> "critical"
                        totalHours > 8 -> "warning"
                        else -> "normal"
                    }
                )
            }
            item {
                ImpactStat(
                    icon = Icons.Default.Battery1Bar,
                    value = "${24 - totalHours} horas",
                    label = "con electricidad",
                    severity = "normal"
                )
            }
            item {
                ImpactStat(
                    icon = Icons.Default.Thermostat,
                    value = when {
                        totalHours > 12 -> "Crítico"
                        totalHours > 8 -> "Severo"
                        else -> "Moderado"
                    },
                    label = "Impacto en confort",
                    severity = when {
                        totalHours > 12 -> "critical"
                        totalHours > 8 -> "warning"
                        else -> "normal"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Warning/Success message
        if (day == 1) {
            Card(
                colors =
                    CardDefaults.cardColors(containerColor = Color(0x33F44336)),
                border = BorderStroke(1.dp, Color(0xFFFF6B6B))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text =
                            "Sin paneles solares, estos apagones afectarán severamente tu confort y productividad",
                        color = Color.White
                    )
                }
            }
        } else {
            Card(
                colors =
                    CardDefaults.cardColors(containerColor = Color(0x334CAF50)),
                border = BorderStroke(1.dp, Color(0xFF4CAF50))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text =
                            "Con tu sistema solar instalado, estos apagones no te afectarán",
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onConfirm(schedules) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF667EEA))
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Comenzar Día $day", fontSize = 18.sp)
        }
        }
    }
}