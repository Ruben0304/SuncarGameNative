package com.suncar.solarsurvivor.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenConfiguration(): ScreenConfiguration {
    val configuration = LocalConfiguration.current
    return ScreenConfiguration(
        screenWidthDp = configuration.screenWidthDp.dp
    )
}