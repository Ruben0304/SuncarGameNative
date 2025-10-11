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
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import suncargamenative.composeapp.generated.resources.Res
import suncargamenative.composeapp.generated.resources.resixed
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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

// Particle data class
data class Particle(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val size: Float,
    val speed: Float,
    val direction: Float,
    val color: Color,
    val lifespan: Float
)

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

    // Enhanced animations
    val infiniteTransition = rememberInfiniteTransition()
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    // Particle system state
    var particles by remember { mutableStateOf(listOf<Particle>()) }
    val maxParticles = if (isMobile) 15 else 25
    
    // Initialize and update particles
    LaunchedEffect(Unit) {
        while (true) {
            particles = particles.filter { it.lifespan > 0 }
            
            if (particles.size < maxParticles) {
                val newParticles = (particles.size until maxParticles).map { id ->
                    Particle(
                        id = id,
                        startX = Random.nextFloat() * screenWidth.value,
                        startY = screenHeight.value + Random.nextFloat() * 200f,
                        size = Random.nextFloat() * 4f + 2f,
                        speed = Random.nextFloat() * 2f + 1f,
                        direction = Random.nextFloat() * 360f,
                        color = when (Random.nextInt(4)) {
                            0 -> Color(0xFFF26729).copy(alpha = 0.6f)
                            1 -> Color(0xFFFDB813).copy(alpha = 0.7f)
                            2 -> Color(0xFF81C784).copy(alpha = 0.5f)
                            else -> Color.White.copy(alpha = 0.4f)
                        },
                        lifespan = Random.nextFloat() * 8f + 5f
                    )
                }
                particles = particles + newParticles
            }
            
            particles = particles.map { particle ->
                particle.copy(
                    startY = particle.startY - particle.speed * 20f,
                    startX = particle.startX + sin(particle.direction) * 0.5f,
                    lifespan = particle.lifespan - 0.1f
                )
            }.filter { it.startY > -100f && it.lifespan > 0 }
            
            delay(50)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF000814),
                        Color(0xFF001D3D),
                        Color(0xFF003566),
                        Color(0xFF000814)
                    ),
                    center = Offset(screenWidth.value * 0.5f, screenHeight.value * 0.3f),
                    radius = screenHeight.value * 0.8f
                )
            )
    ) {
        // Advanced background with multiple layers
        Box(modifier = Modifier.fillMaxSize()) {
            // Animated particle system
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw particles
//                particles.forEach { particle ->
//                    val alpha = (particle.lifespan / 8f).coerceIn(0f, 1f)
//                    drawCircle(
//                        color = particle.color.copy(alpha = alpha),
//                        radius = particle.size.dp.toPx(),
//                        center = Offset(particle.startX.dp.toPx(), particle.startY.dp.toPx())
//                    )
//
//                    // Add glow effect for larger particles
//                    if (particle.size > 3f) {
//                        drawCircle(
//                            color = particle.color.copy(alpha = alpha * 0.3f),
//                            radius = (particle.size * 2f).dp.toPx(),
//                            center = Offset(particle.startX.dp.toPx(), particle.startY.dp.toPx())
//                        )
//                    }
//                }
                
                // Enhanced grid pattern
                val gridSize = 80.dp.toPx()
                val gridAlpha = 0.08f + (glowAlpha * 0.02f)
                
                for (x in 0..(size.width / gridSize).toInt()) {
                    for (y in 0..(size.height / gridSize).toInt()) {
                        val centerX = x * gridSize
                        val centerY = y * gridSize
                        
                        // Animated grid points
                        val animatedRadius = (2f + sin((rotationAngle + x + y) * 0.01f) * 1f).dp.toPx()
                        
                        drawCircle(
                            color = Color(0xFFF26729).copy(alpha = gridAlpha),
                            radius = animatedRadius,
                            center = Offset(centerX, centerY)
                        )
                        
                        // Connect nearby points with lines
                        if (x < (size.width / gridSize).toInt()) {
                            drawLine(
                                color = Color(0xFFFDB813).copy(alpha = gridAlpha * 0.3f),
                                start = Offset(centerX, centerY),
                                end = Offset(centerX + gridSize, centerY),
                                strokeWidth = 0.5.dp.toPx()
                            )
                        }
                    }
                }
            }

            // Enhanced floating elements with more variety
            val floatingElements = remember {
                listOf(
                    Triple(Offset(screenWidth.value * 0.1f, screenHeight.value * 0.2f), 50.dp, 0),
                    Triple(Offset(screenWidth.value * 0.85f, screenHeight.value * 0.15f), 35.dp, 1),
                    Triple(Offset(screenWidth.value * 0.15f, screenHeight.value * 0.8f), 45.dp, 2),
                    Triple(Offset(screenWidth.value * 0.9f, screenHeight.value * 0.7f), 30.dp, 3),
                    Triple(Offset(screenWidth.value * 0.3f, screenHeight.value * 0.1f), 55.dp, 4),
                    Triple(Offset(screenWidth.value * 0.7f, screenHeight.value * 0.9f), 25.dp, 5),
                    Triple(Offset(screenWidth.value * 0.05f, screenHeight.value * 0.6f), 40.dp, 6),
                    Triple(Offset(screenWidth.value * 0.95f, screenHeight.value * 0.4f), 35.dp, 7)
                )
            }
            
            floatingElements.forEachIndexed { index, (offset, size, type) ->
                val rotationMultiplier = if (index % 2 == 0) 1f else -1f
                val floatMultiplier = when (index % 4) {
                    0 -> 1f
                    1 -> -1f
                    2 -> 0.5f
                    else -> -0.5f
                }
                
                Box(
                    modifier = Modifier
                        .offset(
                            x = offset.x.dp + (floatOffset * floatMultiplier).dp,
                            y = offset.y.dp + (floatOffset * 0.3f * floatMultiplier).dp
                        )
                        .size(size)
                        .rotate((rotationAngle * 0.08f) * rotationMultiplier)
                        .scale(0.8f + (glowAlpha * 0.2f))
                        .background(
                            brush = when (type % 3) {
                                0 -> Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFDB813).copy(alpha = 0.15f * glowAlpha),
                                        Color(0xFFF26729).copy(alpha = 0.08f * glowAlpha),
                                        Color.Transparent
                                    )
                                )
                                1 -> Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF81C784).copy(alpha = 0.12f * glowAlpha),
                                        Color(0xFF4CAF50).copy(alpha = 0.06f * glowAlpha),
                                        Color.Transparent
                                    )
                                )
                                else -> Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFE3F2FD).copy(alpha = 0.1f * glowAlpha),
                                        Color(0xFF2196F3).copy(alpha = 0.05f * glowAlpha),
                                        Color.Transparent
                                    )
                                )
                            },
                            shape = when (type % 4) {
                                0 -> CircleShape
                                1 -> RoundedCornerShape(12.dp)
                                2 -> RoundedCornerShape(50)
                                else -> RoundedCornerShape(6.dp)
                            }
                        )
                )
            }
        }

        // Enhanced layout with staggered animations
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = if (isMobile) 16.dp else 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EnhancedGameLogo(logoSize, pulseScale, glowAlpha, rotationAngle, shimmerOffset)
            Spacer(modifier = Modifier.height(verticalSpacing))
            AnimatedGameDescription(titleSize, subtitleSize, pulseScale, shimmerOffset)
            Spacer(modifier = Modifier.height(verticalSpacing * 1.5f))
            EnhancedActionButtons(onStart, buttonHeight, glowAlpha, isMobile, shimmerOffset)
        }

        // Enhanced corner decorations with more effects
        EnhancedCornerDecorations(glowAlpha, floatOffset, shimmerOffset, screenWidth, screenHeight)
    }
}

@Composable
private fun EnhancedGameLogo(size: Dp, pulseScale: Float, glowAlpha: Float, rotationAngle: Float, shimmerOffset: Float) {
    Box(
        modifier = Modifier
            .size(size)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        // Enhanced outer rings with more variety
        repeat(3) { index ->
            val ringSize = size * (0.85f + index * 0.08f)
            val ringRotation = rotationAngle * (1.2f - index * 0.4f) * if (index % 2 == 0) 1f else -1f
            
            Box(
                modifier = Modifier
                    .size(ringSize)
                    .rotate(ringRotation)
                    .border(
                        width = (0.8f + index * 0.4f).dp,
                        brush = Brush.sweepGradient(
                            colors = when (index) {
                                0 -> listOf(
                                    Color.Transparent,
                                    Color(0xFFF26729).copy(alpha = 0.3f * glowAlpha),
                                    Color.Transparent,
                                    Color(0xFFFDB813).copy(alpha = 0.25f * glowAlpha),
                                    Color.Transparent
                                )
                                1 -> listOf(
                                    Color.Transparent,
                                    Color(0xFF81C784).copy(alpha = 0.2f * glowAlpha),
                                    Color.Transparent,
                                    Color(0xFF4CAF50).copy(alpha = 0.15f * glowAlpha),
                                    Color.Transparent
                                )
                                else -> listOf(
                                    Color.Transparent,
                                    Color(0xFFE3F2FD).copy(alpha = 0.15f * glowAlpha),
                                    Color.Transparent,
                                    Color(0xFF2196F3).copy(alpha = 0.1f * glowAlpha),
                                    Color.Transparent
                                )
                            }
                        ),
                        shape = CircleShape
                    )
            )
        }
        
        // Shimmer background effect
        Box(
            modifier = Modifier
                .size(size * 0.9f)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.1f * glowAlpha),
                            Color.Transparent
                        ),
                        start = Offset(shimmerOffset - 200f, shimmerOffset - 200f),
                        end = Offset(shimmerOffset, shimmerOffset)
                    ),
                    shape = CircleShape
                )
        )

        // Main logo image with enhanced effects
        Image(
            painter = painterResource(Res.drawable.resixed),
            contentDescription = "Solar Survivor Logo",
            modifier = Modifier
                .size(size * 0.85f)
                .clip(CircleShape)
                .drawWithContent {
                    drawContent()
                    // Add subtle glow around the image
                    drawCircle(
                        color = Color(0xFFFDB813).copy(alpha = 0.1f * glowAlpha),
                        radius = size.toPx() * 0.45f,
                        center = center
                    )
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun AnimatedGameDescription(titleSize: TextUnit, subtitleSize: TextUnit, pulseScale: Float, shimmerOffset: Float) {
    // Staggered animation for text elements
    val titleAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
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
            color = Color.White.copy(alpha = titleAlpha),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .scale(pulseScale)
                .drawWithContent {
                    drawContent()
                    // Enhanced text glow effect with shimmer
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent
                            ),
                            start = Offset(shimmerOffset - 100f, 0f),
                            end = Offset(shimmerOffset + 100f, 0f)
                        ),
                        alpha = 0.4f
                    )
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
            textAlign = TextAlign.Center,
            modifier = Modifier.drawWithContent {
                drawContent()
                // Subtitle glow with shimmer
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFFF26729).copy(alpha = 0.2f),
                            Color.Transparent
                        ),
                        start = Offset(shimmerOffset - 50f, 0f),
                        end = Offset(shimmerOffset + 50f, 0f)
                    ),
                    alpha = 0.6f
                )
            }
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Sobrevive los apagones cubanos con energía solar",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            ),
            color = Color.White.copy(alpha = 0.85f + (titleAlpha * 0.15f)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EnhancedActionButtons(onStart: (String) -> Unit, buttonHeight: Dp, glowAlpha: Float, isMobile: Boolean, shimmerOffset: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Explicación de modos de juego
        Text(
            text = "Elige tu experiencia:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700),
            textAlign = TextAlign.Center
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Modo Sin Solar - Dificultad Alta
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = { onStart("withoutSolar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight)
                        .drawWithContent {
                            drawContent()
                            // Add button glow effect
                            drawRoundRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFFE57373).copy(alpha = 0.2f * glowAlpha),
                                        Color.Transparent
                                    ),
                                    start = Offset(shimmerOffset - 100f, 0f),
                                    end = Offset(shimmerOffset + 100f, 0f)
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                                alpha = 0.6f
                            )
                        },
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
                            text = if (isMobile) "SIN SOLAR" else "MODO DESAFÍO",
                            fontSize = if (isMobile) 12.sp else 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "🔥 Solo red eléctrica",
                    fontSize = 11.sp,
                    color = Color(0xFFE57373),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Apagones serán duros",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }

            // Modo Con Solar - Experiencia Completa
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onStart("withSolar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight)
                        .drawWithContent {
                            drawContent()
                            // Enhanced button shimmer effect
                            drawRoundRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.3f * glowAlpha),
                                        Color.Transparent
                                    ),
                                    start = Offset(shimmerOffset - 100f, 0f),
                                    end = Offset(shimmerOffset + 100f, 0f)
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                                alpha = 0.8f
                            )
                        },
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
                            text = if (isMobile) "CON SOLAR" else "MODO SOLAR",
                            fontSize = if (isMobile) 12.sp else 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "🌞 Energía renovable",
                    fontSize = 11.sp,
                    color = Color(0xFF81C784),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Libertad energética",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Información adicional sobre el juego
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0x22FFD700)),
            border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.3f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯 Objetivo del Juego",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gestiona tu hogar durante apagones cubanos y descubre los beneficios de la energía solar",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
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
private fun EnhancedCornerDecorations(glowAlpha: Float, floatOffset: Float, shimmerOffset: Float, screenWidth: Dp, screenHeight: Dp) {
    // Enhanced top-left decoration with multiple layers
    Box(
        modifier = Modifier
            .offset((-80).dp + floatOffset.dp, (-80).dp + (floatOffset * 0.5f).dp)
            .size(200.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFF26729).copy(alpha = 0.2f * glowAlpha),
                        Color(0xFFFDB813).copy(alpha = 0.1f * glowAlpha),
                        Color.Transparent
                    ),
                    radius = 100.dp.value
                ),
                shape = CircleShape
            )
    )
    
    // Additional top-left accent
    Box(
        modifier = Modifier
            .offset((-40).dp + (floatOffset * 1.5f).dp, (-40).dp + (floatOffset * 0.8f).dp)
            .size(80.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF81C784).copy(alpha = 0.15f * glowAlpha),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            )
    )

    // Enhanced bottom-right decoration
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(80.dp - floatOffset.dp, 80.dp - (floatOffset * 0.3f).dp)
                .size(180.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFDB813).copy(alpha = 0.18f * glowAlpha),
                            Color(0xFFF26729).copy(alpha = 0.1f * glowAlpha),
                            Color.Transparent
                        ),
                        radius = 90.dp.value
                    ),
                    shape = CircleShape
                )
        )
        
        // Additional bottom-right accent
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(40.dp - (floatOffset * 1.2f).dp, 40.dp - (floatOffset * 0.6f).dp)
                .size(60.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD).copy(alpha = 0.12f * glowAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
    
    // Top-right accent
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(60.dp + floatOffset.dp, (-60).dp - (floatOffset * 0.4f).dp)
                .size(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF2196F3).copy(alpha = 0.1f * glowAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
    
    // Bottom-left accent
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset((-50).dp - floatOffset.dp, 50.dp + (floatOffset * 0.7f).dp)
                .size(90.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.12f * glowAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}