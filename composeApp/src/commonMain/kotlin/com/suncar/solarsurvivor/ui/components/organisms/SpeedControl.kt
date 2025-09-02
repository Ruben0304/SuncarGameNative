package com.suncar.solarsurvivor.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpeedControl(gameSpeed: Long, onSpeedChange: (Long) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text =
                    when (gameSpeed) {
                        5000L -> "Lento"
                        3000L -> "Normal"
                        1500L -> "Rápido"
                        else -> "Muy Rápido"
                    },
                fontSize = 12.sp
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Lento") },
                onClick = {
                    onSpeedChange(5000L)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Normal") },
                onClick = {
                    onSpeedChange(3000L)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Rápido") },
                onClick = {
                    onSpeedChange(1500L)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Muy Rápido") },
                onClick = {
                    onSpeedChange(500L)
                    expanded = false
                }
            )
        }
    }
}
