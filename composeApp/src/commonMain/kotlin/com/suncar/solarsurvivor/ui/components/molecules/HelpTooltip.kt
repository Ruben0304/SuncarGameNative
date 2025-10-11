package com.suncar.solarsurvivor.ui.components.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HelpTooltip(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.HelpOutline,
    tooltipColor: Color = Color(0xFF2A2A3E),
    textColor: Color = Color.White,
    iconColor: Color = Color(0xFFFFD700),
    expanded: Boolean = false,
    onToggle: (() -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(expanded) }
    
    Column(modifier = modifier) {
        // Icono clickeable
        Icon(
            imageVector = icon,
            contentDescription = "Ayuda",
            tint = iconColor,
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    if (onToggle != null) {
                        onToggle()
                    } else {
                        isExpanded = !isExpanded
                    }
                }
        )
        
        // Tooltip expandible
        if (isExpanded) {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = tooltipColor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .widthIn(min = 200.dp, max = 300.dp)
                    .border(1.dp, iconColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = text,
                    color = textColor,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun HintCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Lightbulb,
    backgroundColor: Color = Color(0x33FFD700),
    borderColor: Color = Color(0xFFFFD700),
    dismissible: Boolean = false,
    onDismiss: (() -> Unit)? = null
) {
    var isVisible by remember { mutableStateOf(true) }
    
    if (isVisible) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            border = BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = description,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
                
                if (dismissible) {
                    IconButton(
                        onClick = {
                            isVisible = false
                            onDismiss?.invoke()
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info, // Usar como X
                            contentDescription = "Cerrar",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InlineHelp(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "💡 $text",
        color = Color(0xFFFFD700),
        fontSize = 11.sp,
        modifier = modifier,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun ContextualHint(
    message: String,
    show: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF2A2A3E),
    textColor: Color = Color.White,
    accentColor: Color = Color(0xFFFFD700)
) {
    if (show) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(6.dp),
            modifier = modifier
                .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(accentColor, RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message,
                    color = textColor,
                    fontSize = 11.sp
                )
            }
        }
    }
}