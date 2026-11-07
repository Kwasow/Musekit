package com.kwasow.musekit.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object MusekitUIHelpers {
    // ====== Public methods
    fun gridItemShape(
        index: Int,
        totalItems: Int,
        columns: Int,
    ): Shape {
        val row = index / columns
        val col = index % columns

        val top = row == 0
        val left = col == 0
        val right = col == columns - 1 || index == totalItems - 1
        val bottom = index + columns >= totalItems

        return RoundedCornerShape(
            topStart = if (top && left) 12.dp else 4.dp,
            topEnd = if (top && right) 12.dp else 4.dp,
            bottomStart = if (bottom && left) 12.dp else 4.dp,
            bottomEnd = if (bottom && right) 12.dp else 4.dp,
        )
    }
}
