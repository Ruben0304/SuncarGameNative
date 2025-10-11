package com.suncar.solarsurvivor.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

data class ScreenConfiguration(
    val screenWidthDp: Dp
)

@Composable
expect fun getScreenConfiguration(): ScreenConfiguration