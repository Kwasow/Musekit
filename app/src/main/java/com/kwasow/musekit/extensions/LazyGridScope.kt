package com.kwasow.musekit.extensions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
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
    columns: Int,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(index: Int, item: T) -> GridItemSpan)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T) -> Unit,
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(index, items[index]) } else null,
    span =
        if (span != null) {
            { span(it, items[it]) }
        } else {
            null
        },
    contentType = { index -> contentType(index, items[index]) },
) {
    val dividerColour = MaterialTheme.colorScheme.background

    Box(
        modifier =
            Modifier
                .drawBehind {
                    if (it != items.lastIndex) {
                        drawLine(
                            color = dividerColour,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 3.dp.toPx(),
                        )
                    }

                    if (it % columns != 0) {
                        drawLine(
                            color = dividerColour,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 3.dp.toPx(),
                        )
                    }
                },
    ) {
        itemContent(items[it])
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
