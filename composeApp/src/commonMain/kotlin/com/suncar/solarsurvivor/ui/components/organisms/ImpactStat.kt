package com.suncar.solarsurvivor.ui.components.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.util.getScreenConfiguration


@Composable
fun ImpactStat(icon: ImageVector, value: String, label: String, severity: String) {
    val screenWidth = getScreenConfiguration().screenWidthDp
    val isLarge = screenWidth > 800.dp
    val isCompact = screenWidth <= 500.dp

    val iconSize = when {
        isLarge -> 32.dp
        isCompact -> 28.dp
        else -> 28.dp
    }
    val valueStyle =
        when {
            isLarge -> MaterialTheme.typography.headlineSmall
            isCompact -> MaterialTheme.typography.titleLarge
            else -> MaterialTheme.typography.titleLarge
        }
    val labelStyle =
        when {
            isLarge -> MaterialTheme.typography.bodyMedium
            isCompact -> MaterialTheme.typography.bodyMedium
            else -> MaterialTheme.typography.bodySmall
        }
    val verticalSpacing = if (isCompact) 8.dp else 8.dp
    val cardPadding = when {
        isLarge -> 20.dp
        isCompact -> 16.dp
        else -> 16.dp
    }

    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E))) {
        Column(
            modifier = Modifier.padding(cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint =
                    when (severity) {
                        "critical" -> Color(0xFFFF6B6B)
                        "warning" -> Color(0xFFFFA500)
                        else -> Color(0xFF4CAF50)
                    }
            )
            Spacer(modifier = Modifier.height(verticalSpacing))
            Text(
                text = value,
                style = valueStyle,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
            Text(
                text = label,
                style = labelStyle,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
