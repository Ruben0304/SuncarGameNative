package com.suncar.solarsurvivor.ui.components.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.EnergySource

@Composable
fun EnergyIndicator(energySource: EnergySource) {
    val (icon, text, color) =
        when (energySource) {
            EnergySource.GRID ->
                Triple(Icons.Default.Bolt, "Red eléctrica", Color(0xFF2196F3))
            EnergySource.SOLAR ->
                Triple(Icons.Default.WbSunny, "Panel solar", Color(0xFFFFD700))
            EnergySource.BATTERY ->
                Triple(Icons.Default.Battery1Bar, "Batería", Color(0xFF4CAF50))
            EnergySource.NONE ->
                Triple(Icons.Default.Warning, "Sin energía", Color(0xFFFF6B6B))
        }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, color = Color.White)
        }
    }
}
