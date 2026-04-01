package com.kwasow.musekit.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance

object ContentAlpha {
    val high: Float
        @Composable
        get() =
            contentAlpha(
                highContrastAlpha = HighContrastContentAlpha.HIGH,
                lowContrastAlpha = LowContrastContentAlpha.HIGH,
            )

    val medium: Float
        @Composable
        get() =
            contentAlpha(
                highContrastAlpha = HighContrastContentAlpha.MEDIUM,
                lowContrastAlpha = LowContrastContentAlpha.MEDIUM,
            )

    val disabled: Float
        @Composable
        get() =
            contentAlpha(
                highContrastAlpha = HighContrastContentAlpha.DISABLED,
                lowContrastAlpha = LowContrastContentAlpha.DISABLED,
            )

    @Composable
    private fun contentAlpha(
        highContrastAlpha: Float,
        lowContrastAlpha: Float,
    ): Float {
        val contentColor = LocalContentColor.current
        // TODO: This will cause issues if user can change app theme
        val lightTheme = isSystemInDarkTheme()

        return if (lightTheme) {
            if (contentColor.luminance() > 0.5) highContrastAlpha else lowContrastAlpha
        } else {
            if (contentColor.luminance() < 0.5) highContrastAlpha else lowContrastAlpha
        }
    }
}

private object HighContrastContentAlpha {
    const val HIGH: Float = 1.00f
    const val MEDIUM: Float = 0.74f
    const val DISABLED: Float = 0.38f
}

private object LowContrastContentAlpha {
    const val HIGH: Float = 0.87f
    const val MEDIUM: Float = 0.60f
    const val DISABLED: Float = 0.38f
}
