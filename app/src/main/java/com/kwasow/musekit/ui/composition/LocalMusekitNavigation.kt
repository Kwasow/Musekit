package com.kwasow.musekit.ui.composition

import androidx.compose.runtime.compositionLocalOf

data class MusekitNavigation(
    val navigateToWorklog: () -> Unit = {},
    val navigateBack: () -> Unit = {},
)

val LocalMusekitNavigation =
    compositionLocalOf {
        MusekitNavigation()
    }
