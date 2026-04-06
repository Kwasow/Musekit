package com.kwasow.musekit.utils

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

object ScreenUtils {
    @Composable
    fun isWide(): Boolean {
        val adaptiveInfo = currentWindowAdaptiveInfo()
        return with(adaptiveInfo) {
            windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
        }
    }
}
