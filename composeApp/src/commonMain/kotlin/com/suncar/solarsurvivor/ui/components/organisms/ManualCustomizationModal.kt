package com.suncar.solarsurvivor.ui.components.organisms

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.suncar.solarsurvivor.ui.components.atom.SummaryItem
import com.suncar.solarsurvivor.util.IconWithColor

@Composable
fun ManualCustomizationModal(
    initialPanels: Int,
    initialBatteries: Int,
    onApply: (panels: Int, batteries: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var panels by remember { mutableStateOf(initialPanels) }
    var batteries by remember { mutableStateOf(initialBatteries) }
    var showHelp by remember { mutableStateOf(true) }
    
    // Use a simpler approach for multiplatform compatibility
    val isTablet = false // Default to mobile layout for WASM
    val isMobile = true
    
    val investment = panels * 800 + batteries * 1200
    val monthlyGeneration = panels * 505 * 5 * 30 / 1000
    val monthlySavings = monthlyGeneration * 0.15f
    val roi = if (monthlySavings > 0) investment / (monthlySavings * 12) else 0f
    val autonomy = if (panels > 0) batteries * 5 else 0
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .padding(if (isMobile) 16.dp else 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
                border = BorderStroke(2.dp, Color(0xFFFFD700)),
                modifier = Modifier
                    .fillMaxWidth()
                    .let { if (isTablet) it.fillMaxSize(0.9f) else it }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(if (isMobile) 16.dp else 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(if (isMobile) 24.dp else 32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Personalización Manual",
                                    style = if (isMobile) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                            }
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Ajusta manualmente tu sistema solar según tus necesidades específicas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                    
                    // Help section
                    if (showHelp) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0x332196F3)),
                                border = BorderStroke(1.dp, Color(0xFF2196F3))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Info,
                                                contentDescription = null,
                                                tint = Color(0xFF4FC3F7),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Guía de Configuración",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF4FC3F7)
                                            )
                                        }
                                        IconButton(onClick = { showHelp = false }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Cerrar ayuda",
                                                tint = Color.White.copy(alpha = 0.7f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    Text(
                                        text = "• Casa pequeña (1-2 personas): 2-3 paneles + 1-2 baterías",
                                        color = Color.White.copy(alpha = 0.9f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "• Casa mediana (3-4 personas): 4-6 paneles + 2-3 baterías", 
                                        color = Color.White.copy(alpha = 0.9f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "• Casa grande (5+ personas): 7-10 paneles + 3-4 baterías",
                                        color = Color.White.copy(alpha = 0.9f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Surface(
                                        color = Color(0x33FFD700),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "💡 Cada panel genera ~75 kWh/mes. Una casa promedio consume 200-300 kWh/mes.",
                                            modifier = Modifier.padding(8.dp),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                    
                    // Solar Panels Configuration
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(if (isMobile) 16.dp else 20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.WbSunny,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Paneles Solares JA Solar 505W",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Slider(
                                        value = panels.toFloat(),
                                        onValueChange = { panels = it.toInt() },
                                        valueRange = 0f..12f,
                                        modifier = Modifier.weight(1f),
                                        colors = SliderDefaults.colors(
                                            thumbColor = Color(0xFFFFD700),
                                            activeTrackColor = Color(0xFFFFD700)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Surface(
                                        color = Color(0x33FFD700),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "$panels paneles",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                if (isMobile) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "Generación pico: ${panels * 505}W",
                                            color = Color.White.copy(alpha = 0.8f),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = "Generación mensual: ${monthlyGeneration} kWh",
                                            color = Color.White.copy(alpha = 0.8f),
                                            style = MaterialTheme.typography.bodySmall
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
                                            text = "Generación mensual: ${monthlyGeneration} kWh",
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    
                    // Batteries Configuration
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(if (isMobile) 16.dp else 20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Battery1Bar,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Baterías Pylontech 5kWh",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Slider(
                                        value = batteries.toFloat(),
                                        onValueChange = { batteries = it.toInt() },
                                        valueRange = 0f..6f,
                                        modifier = Modifier.weight(1f),
                                        colors = SliderDefaults.colors(
                                            thumbColor = Color(0xFF4CAF50),
                                            activeTrackColor = Color(0xFF4CAF50)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Surface(
                                        color = Color(0x334CAF50),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "$batteries baterías",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                if (isMobile) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "Capacidad total: ${batteries * 5}kWh",
                                            color = Color.White.copy(alpha = 0.8f),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = "Autonomía nocturna: $autonomy horas",
                                            color = Color.White.copy(alpha = 0.8f),
                                            style = MaterialTheme.typography.bodySmall
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
                                            text = "Autonomía nocturna: $autonomy horas",
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                                
                                if (batteries < 2) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        color = Color(0x33FFA500),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "⚠️ Recomendamos mínimo 2 baterías para pasar la noche",
                                            modifier = Modifier.padding(8.dp),
                                            color = Color(0xFFFFA500),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    
                    // Investment Summary
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                            border = BorderStroke(1.dp, Color(0xFFFFD700))
                        ) {
                            Column(modifier = Modifier.padding(if (isMobile) 16.dp else 20.dp)) {
                                Text(
                                    text = "Resumen de Tu Sistema",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                if (isMobile) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        SummaryItem("Inversión Total:", "$${investment}")
                                        SummaryItem("Ahorro Mensual:", "$${monthlySavings.toInt()}")
                                        SummaryItem("Retorno (ROI):", "${roi.toInt()} años")
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        SummaryItem("Inversión Total:", "$${investment}")
                                        SummaryItem("Ahorro Mensual:", "$${monthlySavings.toInt()}")
                                        SummaryItem("Retorno (ROI):", "${roi.toInt()} años")
                                    }
                                }
                                
                                if (panels == 0 && batteries == 0) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Surface(
                                        color = Color(0x33FF5722),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "⚠️ Selecciona al menos 1 panel o 1 batería para crear tu sistema",
                                            modifier = Modifier.padding(12.dp),
                                            color = Color(0xFFFF5722),
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    
                    // Action Buttons
                    item {
                        if (isMobile) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { 
                                        if (panels > 0 || batteries > 0) {
                                            onApply(panels, batteries)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (panels > 0 || batteries > 0) Color(0xFF4CAF50) else Color.Gray,
                                        contentColor = Color.White
                                    ),
                                    enabled = panels > 0 || batteries > 0
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (panels == 0 && batteries == 0) "Selecciona componentes" else "Aplicar Configuración"
                                    )
                                }
                                OutlinedButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Cancelar")
                                }
                                Button(
                                    onClick = { 
                                        if (panels > 0 || batteries > 0) {
                                            onApply(panels, batteries)
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (panels > 0 || batteries > 0) Color(0xFF4CAF50) else Color.Gray,
                                        contentColor = Color.White
                                    ),
                                    enabled = panels > 0 || batteries > 0
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (panels == 0 && batteries == 0) "Selecciona componentes" else "Aplicar Configuración"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}