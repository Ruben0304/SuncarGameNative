package com.suncar.solarsurvivor.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenConfiguration(): ScreenConfiguration {
    // For iOS, assume large screen by default
    // This could be enhanced to use UIScreen bounds in the future
    return ScreenConfiguration(
        screenWidthDp = 900.dp
    )
}