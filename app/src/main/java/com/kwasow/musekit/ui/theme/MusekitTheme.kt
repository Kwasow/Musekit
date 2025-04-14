package com.kwasow.musekit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ====== Public composables
@Composable
fun MusekitTheme(content: @Composable () -> Unit) {
    val colorScheme = lightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

// ====== Private composables
private val lightScheme =
    lightColorScheme(
        primary = Color(0xFF4A670B),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFCAEF86),
        onPrimaryContainer = Color(0xFF121F00),
        secondary = Color(0xFF596148),
        secondaryContainer = Color(0xFFDDE7C6),
        surfaceVariant = Color(0xFFE1E4D4),
        onSurfaceVariant = Color(0xFF45483C),
    )

private val darkScheme =
    darkColorScheme(
        primary = Color(0xFFAFD36D),
        onPrimary = Color(0xFF223600),
        primaryContainer = Color(0xFF344E00),
        onPrimaryContainer = Color(0xFFCAEF86),
        secondary = Color(0xFFC1CBAB),
        secondaryContainer = Color(0xFF414A32),
        surfaceVariant = Color(0xFF45483C),
        onSurfaceVariant = Color(0xFFC5C8B8),
    )
