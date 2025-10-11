package com.suncar.solarsurvivor.ui.components.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.suncar.solarsurvivor.data.Situation
import com.suncar.solarsurvivor.data.SituationDifficulty
import com.suncar.solarsurvivor.data.SituationOption

@Composable
fun SituationDialog(
    situation: Situation,
    onAnswer: (SituationOption) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedOption by remember { mutableStateOf<SituationOption?>(null) }
    var showResult by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1B3A6B)
            ),
            border = BorderStroke(
                2.dp,
                when (situation.difficulty) {
                    SituationDifficulty.EASY -> Color(0xFF4CAF50)
                    SituationDifficulty.MEDIUM -> Color(0xFFFDB813)
                    SituationDifficulty.HARD -> Color(0xFFF26729)
                }
            ),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with difficulty badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (situation.difficulty) {
                            SituationDifficulty.EASY -> Icons.Default.CheckCircle
                            SituationDifficulty.MEDIUM -> Icons.Default.Warning
                            SituationDifficulty.HARD -> Icons.Default.Error
                        },
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = when (situation.difficulty) {
                            SituationDifficulty.EASY -> Color(0xFF4CAF50)
                            SituationDifficulty.MEDIUM -> Color(0xFFFDB813)
                            SituationDifficulty.HARD -> Color(0xFFF26729)
                        }
                    )

                    Surface(
                        color = when (situation.difficulty) {
                            SituationDifficulty.EASY -> Color(0xFF4CAF50)
                            SituationDifficulty.MEDIUM -> Color(0xFFFDB813)
                            SituationDifficulty.HARD -> Color(0xFFF26729)
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = when (situation.difficulty) {
                                SituationDifficulty.EASY -> "FÁCIL - ${situation.points} pts"
                                SituationDifficulty.MEDIUM -> "MEDIA - ${situation.points} pts"
                                SituationDifficulty.HARD -> "DIFÍCIL - ${situation.points} pts"
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = situation.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = situation.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFB0BEC5),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Options
                situation.options.forEachIndexed { index, option ->
                    val isSelected = selectedOption == option
                    val showCorrectness = showResult && isSelected

                    Card(
                        onClick = {
                            if (!showResult) {
                                selectedOption = option
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                showCorrectness && option.isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.3f)
                                showCorrectness && !option.isCorrect -> Color(0xFFF44336).copy(alpha = 0.3f)
                                isSelected -> Color(0xFF0F2B66)
                                else -> Color(0xFF0A1B40)
                            }
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = when {
                                showCorrectness && option.isCorrect -> Color(0xFF4CAF50)
                                showCorrectness && !option.isCorrect -> Color(0xFFF44336)
                                isSelected -> Color(0xFFFDB813)
                                else -> Color(0xFF37474F)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${('A' + index)}.",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFFFDB813) else Color(0xFFB0BEC5),
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Text(
                                text = option.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )

                            if (showCorrectness) {
                                Icon(
                                    imageVector = if (option.isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (option.isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Explanation (shown after answer)
                if (showResult && selectedOption != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedOption!!.isCorrect)
                                Color(0xFF4CAF50).copy(alpha = 0.2f)
                            else
                                Color(0xFFF44336).copy(alpha = 0.2f)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selectedOption!!.isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFFFDB813),
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp)
                            )

                            Column {
                                Text(
                                    text = if (selectedOption!!.isCorrect) "¡Correcto!" else "Respuesta incorrecta",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = selectedOption!!.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFB0BEC5)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!showResult) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text("Omitir")
                        }

                        Button(
                            onClick = {
                                if (selectedOption != null) {
                                    showResult = true
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = selectedOption != null,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFDB813),
                                disabledContainerColor = Color(0xFF37474F)
                            )
                        ) {
                            Text("Confirmar", color = Color.Black)
                        }
                    } else {
                        Button(
                            onClick = {
                                selectedOption?.let { onAnswer(it) }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedOption?.isCorrect == true)
                                    Color(0xFF4CAF50)
                                else
                                    Color(0xFFF26729)
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selectedOption?.isCorrect == true)
                                        "¡Continuar! +${situation.points} pts"
                                    else
                                        "Continuar (0 pts)",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
