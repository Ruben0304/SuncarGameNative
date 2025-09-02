package com.suncar.solarsurvivor.ui.components.atom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MetricItem(label: String, value: Float) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(120.dp), color = Color.White)
        LinearProgressIndicator(
            progress = value / 100f,
            modifier = Modifier.weight(1f),
            color =
                when {
                    value < 30 -> Color(0xFFFF6B6B)
                    value < 60 -> Color(0xFFFFA500)
                    else -> Color(0xFF4CAF50)
                }
        )
    }
}