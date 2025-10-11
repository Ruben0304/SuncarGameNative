package com.suncar.solarsurvivor.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenConfiguration(): ScreenConfiguration {
    // For WASM/JS, assume large screen by default
    // This could be enhanced to use window.screen dimensions in the future
    return ScreenConfiguration(
        screenWidthDp = 1200.dp
    )
}