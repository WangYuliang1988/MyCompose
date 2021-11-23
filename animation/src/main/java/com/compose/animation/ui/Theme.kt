package com.compose.animation.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun AnimationTheme(content: @Composable () -> Unit) {
    val colors = lightColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Teal200
    )
    MaterialTheme(colors = colors, content = content)
}