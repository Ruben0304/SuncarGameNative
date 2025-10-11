package com.suncar.solarsurvivor.ui.components.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.suncar.solarsurvivor.data.SolarKit

@Composable
fun KitSelectionModal(
    kits: List<SolarKit>,
    selectedKitId: String?,
    isCompact: Boolean,
    onDismiss: () -> Unit,
    onKitSelected: (SolarKit) -> Unit
) {
    val titleStyle =
        if (isCompact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge
    val subtitleStyle =
        if (isCompact) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium

    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(if (isCompact) 16.dp else 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .let { base ->
                            if (isCompact) base else base.widthIn(max = 760.dp)
                        },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
                border = BorderStroke(2.dp, Color(0xFFFFD700)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(if (isCompact) 16.dp else 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = Color(0xFFFFD700)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Kits Suncar recomendados",
                                    style = titleStyle,
                                    color = Color(0xFFFFD700)
                                )
                                Text(
                                    text = "Ofertas reales listas para tu partida",
                                    style = subtitleStyle,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar selección de kits",
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(if (isCompact) 12.dp else 16.dp))

                    Surface(
                        color = Color(0x332196F3),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Selecciona un kit y continuaremos automáticamente con esa configuración.",
                            style = subtitleStyle,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 20.dp))

                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .heightIn(
                                    max = if (isCompact) 480.dp else 540.dp
                                ),
                        verticalArrangement = Arrangement.spacedBy(if (isCompact) 12.dp else 16.dp),
                        contentPadding = PaddingValues(bottom = 4.dp)
                    ) {
                        items(kits) { kit ->
                            KitCard(
                                kit = kit,
                                selected = selectedKitId == kit.id,
                                onClick = { onKitSelected(kit) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 24.dp))
                }
            }
        }
    }
}
