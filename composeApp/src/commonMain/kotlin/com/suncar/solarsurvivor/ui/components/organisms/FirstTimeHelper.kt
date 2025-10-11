package com.suncar.solarsurvivor.ui.components.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FirstTimeGameHelper(
    isFirstTime: Boolean,
    currentDay: Int,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    if (isFirstTime && currentDay == 1) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
            border = BorderStroke(2.dp, Color(0xFFFFD700)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🎮 Bienvenido a Solar Survivor",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                QuickTip(
                    icon = Icons.Default.TouchApp,
                    text = "Toca los electrodomésticos para encenderlos/apagarlos"
                )
                QuickTip(
                    icon = Icons.Default.BatteryAlert,
                    text = "Vigila tu nivel de confort y energía disponible"
                )
                QuickTip(
                    icon = Icons.Default.Warning,
                    text = "Durante apagones, prioriza dispositivos esenciales"
                )
                QuickTip(
                    icon = Icons.Default.Speed,
                    text = "Usa los controles de velocidad para acelerar el tiempo"
                )
            }
        }
    }
}

@Composable
fun EnergySourceHelper(
    energySource: com.suncar.solarsurvivor.data.EnergySource,
    isBlackout: Boolean,
    solarPanels: Int,
    modifier: Modifier = Modifier
) {
    val (message, color, icon) = when {
        isBlackout && solarPanels == 0 -> Triple(
            "⚠️ Sin paneles solares, solo puedes usar la batería (si tienes) durante apagones",
            Color(0xFFFF6B6B),
            Icons.Default.Warning
        )
        isBlackout && solarPanels > 0 -> Triple(
            "✅ Tu sistema solar te mantiene con energía durante el apagón",
            Color(0xFF4CAF50),
            Icons.Default.WbSunny
        )
        energySource == com.suncar.solarsurvivor.data.EnergySource.SOLAR -> Triple(
            "🌞 Usando energía solar limpia y gratuita",
            Color(0xFFFFD700),
            Icons.Default.WbSunny
        )
        energySource == com.suncar.solarsurvivor.data.EnergySource.BATTERY -> Triple(
            "🔋 Funcionando con energía almacenada en baterías",
            Color(0xFF2196F3),
            Icons.Default.Battery4Bar
        )
        energySource == com.suncar.solarsurvivor.data.EnergySource.GRID -> Triple(
            "🏠 Usando electricidad de la red eléctrica",
            Color.White,
            Icons.Default.Home
        )
        else -> Triple(
            "❌ Sin energía disponible - Apaga dispositivos no esenciales",
            Color(0xFFFF6B6B),
            Icons.Default.PowerOff
        )
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ComfortLevelHelper(
    comfortLevel: Float,
    modifier: Modifier = Modifier
) {
    val (message, color) = when {
        comfortLevel < 30 -> "😡 Familia muy molesta - Enciende dispositivos esenciales" to Color(0xFFFF6B6B)
        comfortLevel < 50 -> "😤 Familia frustrada - Mejora las condiciones" to Color(0xFFFFA500)
        comfortLevel < 70 -> "😐 Familia incómoda - Considera encender más dispositivos" to Color(0xFFFFD700)
        comfortLevel < 85 -> "🙂 Familia cómoda - Todo va bien" to Color(0xFF4CAF50)
        else -> "😊 Familia muy feliz - ¡Excelente gestión!" to Color(0xFF4CAF50)
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun QuickTip(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun PowerBalanceHelper(
    solarGeneration: Int,
    totalConsumption: Int,
    batteryCharge: Float,
    maxBatteryCapacity: Float,
    isBlackout: Boolean,
    modifier: Modifier = Modifier
) {
    val balanceMessage = when {
        !isBlackout && solarGeneration > totalConsumption -> 
            "✅ Generando más energía de la que consumes. ¡Excelente!"
        !isBlackout && solarGeneration == totalConsumption -> 
            "⚖️ Energía en equilibrio perfecto"
        !isBlackout && solarGeneration < totalConsumption -> 
            "⚠️ Consumiendo más de lo que generas. La red cubre la diferencia."
        isBlackout && solarGeneration >= totalConsumption -> 
            "🌞 Tu sistema solar cubre todo el consumo durante el apagón"
        isBlackout && batteryCharge > 0 && solarGeneration < totalConsumption -> 
            "🔋 Usando batería para cubrir el déficit energético"
        else -> 
            "❌ Energía insuficiente. Apaga algunos dispositivos."
    }
    
    val color = when {
        solarGeneration >= totalConsumption -> Color(0xFF4CAF50)
        batteryCharge > maxBatteryCapacity * 0.3f -> Color(0xFFFFD700)
        else -> Color(0xFFFF6B6B)
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = balanceMessage,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}