package com.suncar.solarsurvivor.ui.components.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.Appliance
import com.suncar.solarsurvivor.util.IconWithColor


@Composable
fun PriorityItem(appliance: Appliance, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (selected) Color(0x334CAF50) else Color(0xFF1A1A2E)
            ),
        border = if (selected) BorderStroke(2.dp, Color(0xFF4CAF50)) else null
    ) {
        Box {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconWithColor(imageVector = appliance.icon, tint = appliance.iconColor, size = 24.dp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = appliance.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "${appliance.consumption}W",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            if (selected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier =
                        Modifier.align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(16.dp),
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}