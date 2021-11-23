package com.example.crane.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val CraneColorPalette = lightColors(
    primary = CranePurple800,
    secondary = CraneRed,
    surface = CranePurple900,
    onSurface = CraneWhite,
    primaryVariant = CranePurple700
)

@Composable
fun CraneTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = CraneColorPalette,
        typography = CraneTypography,
        content = content
    )
}