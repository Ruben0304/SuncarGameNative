package com.suncar.solarsurvivor.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun getScreenConfiguration(): ScreenConfiguration {
    val bounds = UIScreen.mainScreen.bounds
    val width = bounds.useContents { size.width }.toFloat()
    val calculated = width.dp
    val widthDp = if (width > 0f) calculated else 900.dp
    return ScreenConfiguration(
        screenWidthDp = widthDp
    )
}
