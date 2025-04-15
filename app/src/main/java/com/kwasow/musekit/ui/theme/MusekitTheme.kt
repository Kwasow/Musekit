package com.kwasow.musekit.ui.theme

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ====== Public composables
@Composable
fun MusekitTheme(
    nightMode: Int,
    content: @Composable () -> Unit
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val darkTheme =
        if (nightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            isSystemInDarkTheme()
        } else {
            nightMode == AppCompatDelegate.MODE_NIGHT_YES
        }

    val colorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> darkScheme
        else -> lightScheme
    }

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
