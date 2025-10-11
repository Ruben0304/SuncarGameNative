package com.suncar.solarsurvivor.ui.components.organisms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suncar.solarsurvivor.data.Appliance
import com.suncar.solarsurvivor.util.IconWithColor

@Composable
fun ApplianceCard(
    appliance: Appliance, 
    onClick: () -> Unit,
    isEnergyAvailable: Boolean = true
) {
    // Animaciones de color para transiciones suaves
    val containerColor by animateColorAsState(
        targetValue = when {
            appliance.on -> Color(0x334CAF50)
            !isEnergyAvailable -> Color(0x33555555)
            else -> Color(0xFF1A1A2E)
        },
        animationSpec = tween(300)
    )
    
    val borderColor by animateColorAsState(
        targetValue = when {
            appliance.on -> Color(0xFF4CAF50)
            !isEnergyAvailable -> Color(0xFF555555)
            else -> Color(0x33FFFFFF)
        },
        animationSpec = tween(300)
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { 
            if (isEnergyAvailable || appliance.on) {
                onClick()
            }
        },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(2.dp, borderColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icono con indicador de estado
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    IconWithColor(
                        imageVector = appliance.icon, 
                        tint = if (appliance.on) appliance.iconColor else appliance.iconColor.copy(alpha = 0.5f), 
                        size = 32.dp
                    )
                    
                    // Indicador de estado encendido/apagado
                    if (appliance.on) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-6).dp),
                            color = Color(0xFF4CAF50),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Default.PowerSettingsNew,
                                contentDescription = "Encendido",
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(8.dp),
                                tint = Color.White
                            )
                        }
                    }
                    
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Nombre del dispositivo
                Text(
                    text = appliance.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (appliance.on) Color.White else Color.White.copy(alpha = 0.7f)
                )
                
                // Consumo de energía
                Surface(
                    color = when {
                        appliance.on -> Color(0x334CAF50)
                        else -> Color(0x33FFFFFF)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "${appliance.consumption}W",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = when {
                            appliance.on -> Color(0xFF4CAF50)
                            else -> Color.White.copy(alpha = 0.8f)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                
                // Estado textual
                Text(
                    text = when {
                        appliance.on -> "🟢 Funcionando"
                        !isEnergyAvailable && !appliance.on -> "❌ Sin energía"
                        else -> "⚪ Apagado"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
        }
    }
}
