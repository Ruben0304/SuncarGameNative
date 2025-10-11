package com.suncar.solarsurvivor.ui.screens

import androidx.compose.runtime.Composable

//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccessTime
//import androidx.compose.material.icons.filled.Assessment
//import androidx.compose.material.icons.filled.BatteryChargingFull
//import androidx.compose.material.icons.filled.CalendarToday
//import androidx.compose.material.icons.filled.Description
//import androidx.compose.material.icons.filled.ElectricalServices
//import androidx.compose.material.icons.filled.Event
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.PowerOff
//import androidx.compose.material.icons.filled.Send
//import androidx.compose.material.icons.filled.SolarPower
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material.icons.filled.Timeline
//import androidx.compose.material.icons.filled.TrendingUp
//import androidx.compose.material.icons.filled.WbSunny
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.LinearProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.suncar.solarsurvivor.data.GameStatsCollector
//import com.suncar.solarsurvivor.data.GameEvent
//import com.suncar.solarsurvivor.data.ApplianceUsageStats
//import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
//import com.patrykandpatrick.vico.compose.cartesian.axis.AxisLabelRotation
//import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
//import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
//import com.patrykandpatrick.vico.compose.cartesian.decoration.HorizontalLine
//import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
//import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
//import com.patrykandpatrick.vico.compose.common.component.fixed
//import com.patrykandpatrick.vico.compose.common.component.shadow
//import com.patrykandpatrick.vico.compose.common.component.textComponent
//import com.patrykandpatrick.vico.compose.common.fill
//import com.patrykandpatrick.vico.compose.common.shape.rounded
//import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
//import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
//import com.patrykandpatrick.vico.core.cartesian.Insets
//import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
//import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
//import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
//import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
//import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
//import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
//import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
//import com.patrykandpatrick.vico.core.common.Dimensions
//import com.patrykandpatrick.vico.core.common.shape.Shape
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.LaunchedEffect
//import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
//import com.suncar.solarsurvivor.data.ChartPoint


@Composable
actual fun ComparisonScreen(
    onShare: () -> Unit,
    onQuote: () -> Unit
) {
//    val sessionSummary = GameStatsCollector.sessionSummary.value
//    val chartData = GameStatsCollector.getChartData()
//    val recentEvents = GameStatsCollector.gameEvents.takeLast(5)
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .verticalScroll(rememberScrollState())
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Resumen",
//            style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFFFFD700)
//        )
//
//        Text(
//            text = "Estadísticas completas de tu sesión de juego",
//            style = MaterialTheme.typography.bodyLarge,
//            color = Color.White.copy(alpha = 0.9f),
//            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
//        )
//
//        // System Configuration Section
//        GameSummarySection(
//            title = "Configuración del Sistema",
//            icon = Icons.Default.SolarPower
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    icon = Icons.Default.SolarPower,
//                    value = "${GameStatsCollector.solarPanelsInstalled}",
//                    label = "Paneles Solares",
//                    color = Color(0xFFFFD700)
//                )
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    icon = Icons.Default.BatteryChargingFull,
//                    value = "${GameStatsCollector.batteriesInstalled}",
//                    label = "Baterías",
//                    color = Color(0xFF4CAF50)
//                )
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    icon = Icons.Default.ElectricalServices,
//                    value = "${GameStatsCollector.maxBatteryCapacity.toInt()}kWh",
//                    label = "Capacidad Max",
//                    color = Color(0xFF2196F3)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Game Session Metrics
//        GameSummarySection(
//            title = "Métricas de la Sesión",
//            icon = Icons.Default.Assessment
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    icon = Icons.Default.AccessTime,
//                    value = "${GameStatsCollector.totalGameTimeMinutes}m",
//                    label = "Tiempo Jugado",
//                    color = Color(0xFFFF9800)
//                )
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    icon = Icons.Default.CalendarToday,
//                    value = "${GameStatsCollector.totalDaysPlayed}",
//                    label = "Días Simulados",
//                    color = Color(0xFF9C27B0)
//                )
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    icon = Icons.Default.Timeline,
//                    value = "${GameStatsCollector.totalHoursSimulated}h",
//                    label = "Horas Simuladas",
//                    color = Color(0xFF00BCD4)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Energy Performance
//        GameSummarySection(
//            title = "Rendimiento Energético",
//            icon = Icons.Default.TrendingUp
//        ) {
//            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    StatCard(
//                        modifier = Modifier.weight(1f),
//                        icon = Icons.Default.WbSunny,
//                        value = "${sessionSummary.totalSolarGenerated}W",
//                        label = "Energía Solar",
//                        color = Color(0xFFFFD700)
//                    )
//                    StatCard(
//                        modifier = Modifier.weight(1f),
//                        icon = Icons.Default.ElectricalServices,
//                        value = "${sessionSummary.totalEnergyConsumed}W",
//                        label = "Consumo Total",
//                        color = Color(0xFFFF5722)
//                    )
//                }
//
//                if (chartData.energyGenerationOverTime.isNotEmpty()) {
//                    EnergyChart(
//                        generationData = chartData.energyGenerationOverTime,
//                        consumptionData = chartData.energyConsumptionOverTime,
//                        modifier = Modifier.height(200.dp)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Comfort Performance
//        GameSummarySection(
//            title = "Rendimiento del Hogar",
//            icon = Icons.Default.Home
//        ) {
//            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    StatCard(
//                        modifier = Modifier.weight(1f),
//                        icon = Icons.Default.TrendingUp,
//                        value = "${sessionSummary.averageComfort.toInt()}%",
//                        label = "Comfort Promedio",
//                        color = Color(0xFF4CAF50)
//                    )
//                    StatCard(
//                        modifier = Modifier.weight(1f),
//                        icon = Icons.Default.PowerOff,
//                        value = "${sessionSummary.totalBlackoutHours}h",
//                        label = "Horas sin Luz",
//                        color = Color(0xFFFF6B6B)
//                    )
//                }
//
//                if (chartData.comfortOverTime.isNotEmpty()) {
//                    ComfortChart(
//                        comfortData = chartData.comfortOverTime,
//                        blackoutPeriods = chartData.blackoutPeriods,
//                        modifier = Modifier.height(180.dp)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Efficiency Score
//        Card(
//            colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
//            border = BorderStroke(2.dp, Color(0xFFFFD700)),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Star,
//                    contentDescription = null,
//                    modifier = Modifier.size(48.dp),
//                    tint = Color(0xFFFFD700)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Puntuación de Eficiencia",
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFFFFD700)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = "${sessionSummary.gameEfficiencyScore.toInt()}%",
//                    style = MaterialTheme.typography.displayMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFFFFD700)
//                )
//
//                LinearProgressIndicator(
//                    progress = { sessionSummary.gameEfficiencyScore / 100f },
//                    modifier = Modifier
//                        .fillMaxWidth(0.8f)
//                        .padding(vertical = 16.dp),
//                    color = Color(0xFFFFD700),
//                    trackColor = Color(0x33FFD700)
//                )
//
//                Text(
//                    text = when {
//                        sessionSummary.gameEfficiencyScore >= 80f -> "¡Excelente gestión energética!"
//                        sessionSummary.gameEfficiencyScore >= 60f -> "Buen rendimiento energético"
//                        sessionSummary.gameEfficiencyScore >= 40f -> "Rendimiento promedio"
//                        else -> "Hay margen de mejora"
//                    },
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = Color.White.copy(alpha = 0.9f)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Appliance Usage
//        if (chartData.applianceUsageStats.isNotEmpty()) {
//            GameSummarySection(
//                title = "Uso de Electrodomésticos",
//                icon = Icons.Default.Home
//            ) {
//                ApplianceUsageSection(chartData.applianceUsageStats)
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//
//        // Recent Events
//        if (recentEvents.isNotEmpty()) {
//            GameSummarySection(
//                title = "Eventos Recientes",
//                icon = Icons.Default.Event
//            ) {
//                LazyColumn(
//                    modifier = Modifier.height(200.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    items(recentEvents) { event ->
//                        EventItem(event = event)
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//        }
//
//        // Actions
//        Card(
//            colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF)),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.padding(24.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                OutlinedButton(
//                    onClick = onShare,
//                    colors = ButtonDefaults.outlinedButtonColors(
//                        contentColor = Color.White
//                    ),
//                    border = BorderStroke(1.dp, Color.White),
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Icon(Icons.Default.Send, contentDescription = null)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Compartir")
//                }
//
//                Button(
//                    onClick = onQuote,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFFFFD700)
//                    ),
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Icon(Icons.Default.Description, contentDescription = null)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Solicitar Cotización", color = Color.Black)
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//    }
//}
//
//@Composable
//private fun GameSummarySection(
//    title: String,
//    icon: ImageVector,
//    content: @Composable () -> Unit
//) {
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
//        border = BorderStroke(1.dp, Color(0x33FFD700)),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(20.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = null,
//                    tint = Color(0xFFFFD700),
//                    modifier = Modifier.size(24.dp)
//                )
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            content()
//        }
//    }
//}
//
//@Composable
//private fun StatCard(
//    modifier: Modifier = Modifier,
//    icon: ImageVector,
//    value: String,
//    label: String,
//    color: Color
//) {
//    Card(
//        modifier = modifier,
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF252545)),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = null,
//                tint = color,
//                modifier = Modifier.size(24.dp)
//            )
//
//            Text(
//                text = value,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                color = color
//            )
//
//            Text(
//                text = label,
//                style = MaterialTheme.typography.bodySmall,
//                color = Color.White.copy(alpha = 0.7f)
//            )
//        }
//    }
//}
//
//@Composable
//private fun EnergyChart(
//    generationData: List<ChartPoint>,
//    consumptionData: List<ChartPoint>,
//    modifier: Modifier = Modifier
//) {
//    if (generationData.isEmpty() && consumptionData.isEmpty()) return
//
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF252545)),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = "Generación vs Consumo",
//                style = MaterialTheme.typography.titleMedium,
//                color = Color.White,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            val modelProducer = remember { CartesianChartModelProducer() }
//
//            LaunchedEffect(generationData, consumptionData) {
//                modelProducer.runTransaction {
//                    if (generationData.isNotEmpty() && consumptionData.isNotEmpty()) {
//                        lineSeries {
//                            series(
//                                x = generationData.map { it.x.toDouble() },
//                                y = generationData.map { it.y.toDouble() }
//                            )
//                            series(
//                                x = consumptionData.map { it.x.toDouble() },
//                                y = consumptionData.map { it.y.toDouble() }
//                            )
//                        }
//                    }
//                }
//            }
//
//            CartesianChartHost(
//                chart = rememberCartesianChart(
//                    LineCartesianLayer(
//                        lineProvider = LineCartesianLayer.LineProvider.series(
//                            LineCartesianLayer.rememberLine(
//                                fill = remember { LineCartesianLayer.LineFill.single(fill(Color(0xFFFFD700))) },
//                            ),
//                            LineCartesianLayer.rememberLine(
//                                fill = remember { LineCartesianLayer.LineFill.single(fill(Color(0xFFFF5722))) },
//                            )
//                        ),
//                        pointProvider = LineCartesianLayer.PointProvider.single(
//                            LineCartesianLayer.rememberPoint(
//                                component = textComponent {
//                                    color = Color.Transparent
//                                    margins = Dimensions.ALL.of(0.dp)
//                                    padding = Dimensions.ALL.of(0.dp)
//                                },
//                                size = 0.dp
//                            )
//                        )
//                    ),
//                    startAxis = VerticalAxis.rememberStart(
//                        line = null,
//                        tick = null,
//                        label = textComponent {
//                            color = Color.White.copy(alpha = 0.7f)
//                            textSize = 12.sp
//                            margins = Dimensions.ALL.of(4.dp)
//                        },
//                        labelRotationDegrees = 0f
//                    ),
//                    bottomAxis = HorizontalAxis.rememberBottom(
//                        line = null,
//                        tick = null,
//                        label = textComponent {
//                            color = Color.White.copy(alpha = 0.7f)
//                            textSize = 12.sp
//                            margins = Dimensions.ALL.of(4.dp)
//                        },
//                        labelRotationDegrees = 0f,
//                        itemPlacer = AxisItemPlacer.step(step = 4.0, addExtremeLabelPadding = true)
//                    )
//                ),
//                modelProducer = modelProducer,
//                modifier = modifier.height(200.dp)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Legend
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                LegendItem("Generación", Color(0xFFFFD700))
//                LegendItem("Consumo", Color(0xFFFF5722))
//            }
//        }
//    }
//}
//
//@Composable
//private fun ComfortChart(
//    comfortData: List<ChartPoint>,
//    blackoutPeriods: List<Float>,
//    modifier: Modifier = Modifier
//) {
//    if (comfortData.isEmpty()) return
//
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF252545)),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = "Nivel de Comfort",
//                style = MaterialTheme.typography.titleMedium,
//                color = Color.White,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            val modelProducer = remember { CartesianChartModelProducer() }
//
//            LaunchedEffect(comfortData) {
//                modelProducer.runTransaction {
//                    if (comfortData.isNotEmpty()) {
//                        lineSeries {
//                            series(
//                                x = comfortData.map { it.x.toDouble() },
//                                y = comfortData.map { it.y.toDouble() }
//                            )
//                        }
//                    }
//                }
//            }
//
//            val decorations = remember(blackoutPeriods) {
//                blackoutPeriods.map { hour ->
//                    HorizontalLine(
//                        y = { _, _, _ -> 0.0 },
//                        line = LineCartesianLayer.rememberLine(
//                            fill = remember { LineCartesianLayer.LineFill.single(fill(Color(0xFFFF6B6B).copy(alpha = 0.1f))) },
//                            thickness = 100.dp
//                        ),
//                        verticalLabelPosition = HorizontalLine.LabelVerticalPosition.Top
//                    )
//                }
//            }
//
//            CartesianChartHost(
//                chart = rememberCartesianChart(
//                    LineCartesianLayer(
//                        lineProvider = LineCartesianLayer.LineProvider.series(
//                            LineCartesianLayer.rememberLine(
//                                fill = remember { LineCartesianLayer.LineFill.single(fill(Color(0xFF4CAF50))) },
//                                thickness = 3.dp,
//                                pathEffect = null,
//                                cap = androidx.compose.ui.graphics.StrokeCap.Round
//                            )
//                        ),
//                        pointProvider = LineCartesianLayer.PointProvider.single(
//                            LineCartesianLayer.rememberPoint(
//                                component = textComponent {
//                                    shape = Shape.rounded(allPercent = 50)
//                                    color = Color(0xFF4CAF50)
//                                    textColor = Color.White
//                                    textSize = 0.sp
//                                    shadow {
//                                        radius = 6.dp
//                                        color = Color.Black.copy(alpha = 0.3f)
//                                    }
//                                },
//                                size = 8.dp
//                            )
//                        ),
//                        rangeProvider = LineCartesianLayer.RangeProvider.fixed(minY = 0.0, maxY = 100.0)
//                    ),
//                    startAxis = VerticalAxis.rememberStart(
//                        line = null,
//                        tick = null,
//                        label = textComponent {
//                            color = Color.White.copy(alpha = 0.7f)
//                            textSize = 12.sp
//                            margins = Dimensions.ALL.of(4.dp)
//                        },
//                        labelRotationDegrees = 0f,
//                        valueFormatter = CartesianValueFormatter { value, _, _ -> "${value.toInt()}%" }
//                    ),
//                    bottomAxis = HorizontalAxis.rememberBottom(
//                        line = null,
//                        tick = null,
//                        label = textComponent {
//                            color = Color.White.copy(alpha = 0.7f)
//                            textSize = 12.sp
//                            margins = Dimensions.ALL.of(4.dp)
//                        },
//                        labelRotationDegrees = 0f,
//                        itemPlacer = AxisItemPlacer.step(step = 4.0, addExtremeLabelPadding = true),
//                        valueFormatter = CartesianValueFormatter { value, _, _ -> "${value.toInt()}h" }
//                    )
//                ),
//                modelProducer = modelProducer,
//                modifier = modifier.height(180.dp)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Legend
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                LegendItem("Comfort", Color(0xFF4CAF50))
//                if (blackoutPeriods.isNotEmpty()) {
//                    LegendItem("Apagones", Color(0xFFFF6B6B))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun LegendItem(
//    label: String,
//    color: Color
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(12.dp)
//                .background(color, RoundedCornerShape(2.dp))
//        )
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.White.copy(alpha = 0.7f)
//        )
//    }
//}
//
//@Composable
//private fun ApplianceUsageSection(
//    applianceStats: Map<String, ApplianceUsageStats>
//) {
//    LazyRow(
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        items(applianceStats.entries.toList()) { (key, stats) ->
//            Card(
//                colors = CardDefaults.cardColors(containerColor = Color(0xFF252545)),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ElectricalServices,
//                        contentDescription = null,
//                        tint = Color(0xFF2196F3),
//                        modifier = Modifier.size(24.dp)
//                    )
//
//                    Text(
//                        text = stats.applianceName,
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Medium,
//                        color = Color.White
//                    )
//
//                    Text(
//                        text = "${stats.totalOnTime}h",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color(0xFF2196F3)
//                    )
//
//                    Text(
//                        text = "${stats.totalConsumption.toInt()}W",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color.White.copy(alpha = 0.7f)
//                    )
//
//                    Text(
//                        text = "${stats.timesToggled} cambios",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color.White.copy(alpha = 0.5f)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun EventItem(
//    event: GameEvent
//) {
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF252545)),
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Icon(
//                imageVector = when (event.type) {
//                    "SESSION_START" -> Icons.Default.Timeline
//                    "SESSION_END" -> Icons.Default.Assessment
//                    "SOLAR_CONFIG" -> Icons.Default.SolarPower
//                    else -> Icons.Default.Event
//                },
//                contentDescription = null,
//                tint = Color(0xFFFFD700),
//                modifier = Modifier.size(20.dp)
//            )
//
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = event.description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.White
//                )
//
//                Text(
//                    text = "Día ${event.day}, Hora ${event.hour}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.White.copy(alpha = 0.6f)
//                )
//            }
//        }
//    }
}