package com.kwasow.musekit.extensions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

// ====== Public extensions
inline fun <T> LazyGridScope.itemsWithDividers(
    items: List<T>,
    state: LazyGridState,
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, item: T) -> Unit,
) = itemsIndexed(items) { index, item ->
    // TODO: @FrequentlyChanging
    val layoutInfo = state.layoutInfo
    val columns = layoutInfo.viewportSize.width
    val rows = layoutInfo.viewportSize.height

    val itemInfo = layoutInfo.visibleItemsInfo.getOrNull(index)
    val itemColumn = itemInfo?.column ?: 0
    val itemRow = itemInfo?.row ?: 0

    val dividerColour = MaterialTheme.colorScheme.background

    Box(
        modifier =
            Modifier
                .drawBehind {
                    if (itemColumn != columns) {
                        drawLine(
                            color = dividerColour,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 3.dp.toPx(),
                        )
                    }

                    if (itemRow != rows) {
                        drawLine(
                            color = dividerColour,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 3.dp.toPx(),
                        )
                    }
                },
    ) {
        itemContent(index, item)
    }
}

// ====== Private composables
@Composable
fun ListDivider() {
    HorizontalDivider(
        thickness = 3.dp,
        color = MaterialTheme.colorScheme.background,
    )
}
