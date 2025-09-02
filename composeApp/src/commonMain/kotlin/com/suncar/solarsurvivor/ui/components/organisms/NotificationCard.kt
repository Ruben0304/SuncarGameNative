package com.suncar.solarsurvivor.ui.components.organisms

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.Notification
import com.suncar.solarsurvivor.data.NotificationType


@Composable
fun NotificationCard(notification: Notification) {
    val backgroundColor =
        when (notification.type) {
            NotificationType.SUCCESS -> Color(0x334CAF50)
            NotificationType.WARNING -> Color(0x33FFA500)
            NotificationType.ERROR -> Color(0x33FF6B6B)
            NotificationType.INFO -> Color(0x332196F3)
        }

    val borderColor =
        when (notification.type) {
            NotificationType.SUCCESS -> Color(0xFF4CAF50)
            NotificationType.WARNING -> Color(0xFFFFA500)
            NotificationType.ERROR -> Color(0xFFFF6B6B)
            NotificationType.INFO -> Color(0xFF2196F3)
        }

    val icon =
        when (notification.type) {
            NotificationType.SUCCESS -> Icons.Default.Check
            NotificationType.WARNING -> Icons.Default.Warning
            NotificationType.ERROR -> Icons.Default.Error
            NotificationType.INFO -> Icons.Default.Info
        }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.animateContentSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = notification.message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            notification.recommendation?.let { recommendation ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recommendation,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
