package com.suncar.solarsurvivor.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import com.suncar.solarsurvivor.util.getScreenConfiguration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suncar.solarsurvivor.data.BlackoutSchedule


@Composable
fun ScheduleRow(
    index: Int,
    schedule: BlackoutSchedule,
    onStartChange: (Int) -> Unit,
    onEndChange: (Int) -> Unit,
    onRemove: (() -> Unit)?
) {
    val screenConfiguration = getScreenConfiguration()
    val screenWidth = screenConfiguration.screenWidthDp
    
    // Responsive spacing and sizing
    val horizontalSpacing = when {
        screenWidth > 800.dp -> 16.dp
        screenWidth > 600.dp -> 12.dp
        else -> 8.dp
    }
    
    val titleWidth = when {
        screenWidth > 800.dp -> 120.dp
        screenWidth > 600.dp -> 100.dp
        else -> 80.dp
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Apagón $index",
            style = if (screenWidth > 600.dp) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700),
            modifier = Modifier.widthIn(min = titleWidth, max = titleWidth)
        )

        Text(
            "Desde:", 
            color = Color.White.copy(alpha = 0.8f),
            style = if (screenWidth > 600.dp) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(horizontalSpacing))

        var startExpanded by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(
                onClick = { startExpanded = true },
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
            ) { Text("${schedule.start.toString().padStart(2, '0')}:00") }
            DropdownMenu(
                expanded = startExpanded,
                onDismissRequest = { startExpanded = false }
            ) {
                (0..23).forEach { hour ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                "${hour.toString().padStart(2, '0')}:00"
                            )
                        },
                        onClick = {
                            onStartChange(hour)
                            startExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(horizontalSpacing))
        Text(
            "Hasta:", 
            color = Color.White.copy(alpha = 0.8f),
            style = if (screenWidth > 600.dp) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(horizontalSpacing))

        var endExpanded by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(
                onClick = { endExpanded = true },
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
            ) { Text("${schedule.end.toString().padStart(2, '0')}:00") }
            DropdownMenu(
                expanded = endExpanded,
                onDismissRequest = { endExpanded = false }
            ) {
                (schedule.start + 1..23).forEach { hour ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                "${hour.toString().padStart(2, '0')}:00"
                            )
                        },
                        onClick = {
                            onEndChange(hour)
                            endExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(horizontalSpacing))

        // Duration badge
        Surface(
            color = Color(0x33FFD700), 
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "${schedule.end - schedule.start}h",
                modifier = Modifier.padding(
                    horizontal = if (screenWidth > 600.dp) 8.dp else 6.dp,
                    vertical = if (screenWidth > 600.dp) 4.dp else 3.dp
                ),
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Bold,
                style = if (screenWidth > 600.dp) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        onRemove?.let {
            IconButton(onClick = it) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color(0xFFFF6B6B)
                )
            }
        }
    }
}