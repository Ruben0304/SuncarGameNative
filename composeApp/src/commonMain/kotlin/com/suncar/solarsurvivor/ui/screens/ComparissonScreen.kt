package com.suncar.solarsurvivor.ui.screens

import androidx.compose.runtime.Composable

@Composable
expect fun ComparisonScreen(
    onShare: () -> Unit,
    onQuote: () -> Unit
)