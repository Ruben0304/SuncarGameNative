package com.suncar.solarsurvivor.ui.components.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suncar.solarsurvivor.data.Appliance
import com.suncar.solarsurvivor.util.IconWithColor

@Composable
fun ApplianceCard(appliance: Appliance, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    when {
                        appliance.on -> Color(0x334CAF50)
//                        appliance.essential && !appliance.on ->
//                            Color(0x33FF6B6B)
                        else -> Color(0xFF1A1A2E)
                    }
            ),
        border =
            when {
                appliance.on -> BorderStroke(2.dp, Color(0xFF4CAF50))
//                appliance.essential && !appliance.on ->
//                    BorderStroke(2.dp, Color(0xFFFF6B6B))
                else -> null
            }
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconWithColor(imageVector = appliance.icon, tint = appliance.iconColor, size = 32.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = appliance.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${appliance.consumption}W",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

//            if (appliance.on) {
//                Surface(
//                    modifier = Modifier.align(Alignment.TopEnd),
//                    color = Color(0xFF4CAF50),
//                    shape = CircleShape
//                ) {
//                    Text(
//                        text = "✓",
//                        modifier = Modifier.padding(4.dp),
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//                }
//            }
        }
    }
}
