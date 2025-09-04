package com.suncar.solarsurvivor.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import org.jetbrains.compose.resources.painterResource
import suncargamenative.composeapp.generated.resources.Res
import suncargamenative.composeapp.generated.resources.resixed

@Composable
fun StartScreen(onStart: (String) -> Unit) {
    // Detectar el estado de BoxWithConstraints para responsive design
    BoxWithConstraints {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val isLandscape = screenWidth > screenHeight
        val isMobile = screenWidth < 600.dp

        // Tamaños responsivos (más pequeños)
        val logoSize = if (isMobile) 150.dp else 230.dp
        val titleSize = if (isMobile) 32.sp else 40.sp
        val subtitleSize = if (isMobile) 12.sp else 16.sp
        val buttonHeight = if (isMobile) 48.dp else 52.dp
        val horizontalPadding = if (isMobile) 24.dp else 48.dp
        val verticalSpacing = if (isMobile) 24.dp else 32.dp

        StartScreenContent(
            onStart = onStart,
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            isLandscape = isLandscape,
            isMobile = isMobile,
            logoSize = logoSize,
            titleSize = titleSize,
            subtitleSize = subtitleSize,
            buttonHeight = buttonHeight,
            horizontalPadding = horizontalPadding,
            verticalSpacing = verticalSpacing
        )
    }
}

@Composable
private fun StartScreenContent(
    onStart: (String) -> Unit,
    screenWidth: Dp,
    screenHeight: Dp,
    isLandscape: Boolean,
    isMobile: Boolean,
    logoSize: Dp,
    titleSize: TextUnit,
    subtitleSize: TextUnit,
    buttonHeight: Dp,
    horizontalPadding: Dp,
    verticalSpacing: Dp
) {

    // Animaciones mejoradas
    val infiniteTransition = rememberInfiniteTransition()
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                color = Color. Black
            //                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color.Black, // Mucho más oscuro
//                        Color(0xFF081526), // Más oscuro
//                        Color(0xFF0B1A33), // Más oscuro
//                        Color(0xFF081526)  // Más oscuro
//                    )
//                )
            )
    ) {
        // Fondo avanzado con múltiples capas
        Box(modifier = Modifier.fillMaxSize()) {
            // Patrón hexagonal sutil
            Canvas(modifier = Modifier.fillMaxSize()) {
                val hexSize = 60.dp.toPx()
                val hexHeight = hexSize * 0.866f
                
                for (row in 0..((size.height / hexHeight).toInt() + 2)) {
                    for (col in 0..((size.width / hexSize).toInt() + 2)) {
                        val x = col * hexSize * 0.75f + if (row % 2 == 1) hexSize * 0.375f else 0f
                        val y = row * hexHeight
                        
                        drawCircle(
                            color = Color(0xFFF26729).copy(alpha = 0.03f),
                            radius = 2.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }
            }

            // Elementos flotantes mejorados
            listOf(
                Pair(Offset(screenWidth.value * 0.1f, screenHeight.value * 0.2f), 40.dp),
                Pair(Offset(screenWidth.value * 0.85f, screenHeight.value * 0.15f), 30.dp),
                Pair(Offset(screenWidth.value * 0.15f, screenHeight.value * 0.8f), 35.dp),
                Pair(Offset(screenWidth.value * 0.9f, screenHeight.value * 0.7f), 25.dp),
                Pair(Offset(screenWidth.value * 0.3f, screenHeight.value * 0.1f), 45.dp),
                Pair(Offset(screenWidth.value * 0.7f, screenHeight.value * 0.9f), 20.dp)
            ).forEachIndexed { index, (offset, size) ->
                Box(
                    modifier = Modifier
                        .offset(
                            x = offset.x.dp + (floatOffset * (if (index % 2 == 0) 1f else -1f)).dp,
                            y = offset.y.dp + (floatOffset * 0.5f * (if (index % 3 == 0) 1f else -1f)).dp
                        )
                        .size(size)
                        .rotate((rotationAngle * 0.1f) * (if (index % 2 == 0) 1f else -1f))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFDB813).copy(alpha = 0.1f * glowAlpha),
                                    Color(0xFFF26729).copy(alpha = 0.05f * glowAlpha),
                                    Color.Transparent
                                )
                            ),
                            shape = if (index % 3 == 0) CircleShape else RoundedCornerShape(8.dp)
                        )
                )
            }
        }

        // Layout vertical único
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = if (isMobile) 16.dp else 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GameLogo(logoSize, pulseScale, glowAlpha, rotationAngle)
            Spacer(modifier = Modifier.height(verticalSpacing))
            GameDescription(titleSize, subtitleSize, pulseScale)
            Spacer(modifier = Modifier.height(verticalSpacing * 1.5f))
            ActionButtons(onStart, buttonHeight, glowAlpha, isMobile)
        }

        // Elementos decorativos en las esquinas mejorados
        CornerDecorations(glowAlpha, floatOffset)
    }
}

@Composable
private fun GameLogo(size: Dp, pulseScale: Float, glowAlpha: Float, rotationAngle: Float) {
    Box(
        modifier = Modifier
            .size(size)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        // Anillos externos decorativos más sutiles
        repeat(2) { index ->
            Box(
                modifier = Modifier
                    .size(size * (0.9f + index * 0.1f))
                    .rotate(rotationAngle * (1f - index * 0.5f))
                    .border(
                        width = (0.5f + index * 0.3f).dp,
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFF26729).copy(alpha = 0.2f * glowAlpha),
                                Color.Transparent,
                                Color(0xFFFDB813).copy(alpha = 0.2f * glowAlpha),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Logo de imagen real
        Image(
            painter = painterResource(Res.drawable.resixed),
            contentDescription = "Solar Survivor Logo",
            modifier = Modifier
                .size(size * 0.85f)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun GameDescription(titleSize: TextUnit, subtitleSize: TextUnit, pulseScale: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SOLAR SURVIVOR",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = titleSize,
                letterSpacing = 3.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .scale(pulseScale)
                .drawWithContent {
                    drawContent()
                    // Efecto de glow en el texto
                    drawContent()
                }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "⚡ SUPERVIVENCIA ENERGÉTICA ⚡",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = subtitleSize,
                letterSpacing = 2.sp
            ),
            color = Color(0xFFF26729),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Sobrevive los apagones cubanos con energía solar",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            ),
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionButtons(onStart: (String) -> Unit, buttonHeight: Dp, glowAlpha: Float, isMobile: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Botón Desafío Extremo - Rojo suave
        OutlinedButton(
            onClick = { onStart("withoutSolar") },
            modifier = Modifier
                .weight(1f)
                .height(buttonHeight),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFE57373)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Color(0xFFE57373).copy(alpha = glowAlpha))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Bolt,
                    contentDescription = null,
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EMPEZAR SUFRIMIENTO",
                    fontSize = if (isMobile) 12.sp else 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Botón Modo Solar - Verde suave
        Button(
            onClick = { onStart("withSolar") },
            modifier = Modifier
                .weight(1f)
                .height(buttonHeight),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF81C784),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.WbSunny,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EXPERIMENTAR LIBERTAD",
                    fontSize = if (isMobile) 12.sp else 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun GameStats(isMobile: Boolean) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = if (isMobile) 0.dp else 16.dp)
    ) {
        items(
            listOf(
                Triple(Icons.Filled.Timer, "15-20", "MINUTOS"),
                Triple(Icons.Filled.EmojiEvents, "∞", "LOGROS"),
                Triple(Icons.Filled.TrendingUp, "REAL", "DATOS CUBA"),
                Triple(Icons.Filled.Groups, "1-4", "JUGADORES")
            )
        ) { (icon, value, label) ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = if (isMobile) 80.dp else 120.dp, height = if (isMobile) 80.dp else 100.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F2B66).copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFFFDB813),
                        modifier = Modifier.size(if (isMobile) 20.dp else 28.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value,
                        fontSize = if (isMobile) 16.sp else 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = label,
                        fontSize = if (isMobile) 8.sp else 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CornerDecorations(glowAlpha: Float, floatOffset: Float) {
    // Decoración superior izquierda
    Box(
        modifier = Modifier
            .offset((-60).dp + floatOffset.dp, (-60).dp + (floatOffset * 0.5f).dp)
            .size(150.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFF26729).copy(alpha = 0.15f * glowAlpha),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            )
    )

    // Decoración inferior derecha
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(60.dp - floatOffset.dp, 60.dp - (floatOffset * 0.3f).dp)
                .size(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFDB813).copy(alpha = 0.12f * glowAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}