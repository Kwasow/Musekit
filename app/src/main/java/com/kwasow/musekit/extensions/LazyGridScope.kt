package com.kwasow.musekit.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.ui.components.ListEntry
import com.kwasow.musekit.ui.components.MusekitUIHelpers
import com.kwasow.musekit.ui.components.SectionTitle

fun LazyGridScope.musekitHeader(title: String) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        SectionTitle(title = title)
    }
}

fun <T> LazyGridScope.musekitItems(
    items: List<T>,
    itemContent: @Composable LazyGridItemScope.(index: Int, item: T) -> Unit,
) {
    var columns by mutableIntStateOf(1)

    itemsIndexed(
        items = items,
        span = { _: Int, _: T ->
            columns = maxLineSpan
            GridItemSpan(1)
        },
    ) { index, item ->
        val shape =
            MusekitUIHelpers.gridItemShape(
                index = index,
                totalItems = items.size,
                columns = columns,
            )

        Column(
            modifier =
                Modifier
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            itemContent(index, item)
        }
    }
}

// ====== Previews
private data class PreviewEntry(
    val icon: VectorPainter,
    val header: String,
    val description: String?,
)

@Preview
@Composable
private fun ListPreview() {
    val entries =
        listOf(
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 1",
                description = "This is the setting description",
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 2 (no description)",
                description = null,
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 3",
                description =
                    "This is the setting description that is so long that it doesn't" +
                        "fit on the screen",
            ),
        )

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        musekitHeader("General")

        musekitItems(entries) { _, item ->
            ListEntry(
                icon = item.icon,
                header = item.header,
                description = item.description,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun GridPreview() {
    val entries =
        listOf(
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 1",
                description = "This is the setting description",
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 2 (no description)",
                description = null,
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 3",
                description =
                    "This is the setting description that is so long that it doesn't" +
                        "fit on the screen",
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 1",
                description = "This is the setting description",
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 2 (no description)",
                description = null,
            ),
        )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        musekitHeader("General")

        musekitItems(entries) { _, item ->
            ListEntry(
                icon = item.icon,
                header = item.header,
                description = item.description,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun MultiGridPreview() {
    val entries =
        listOf(
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 1",
                description = "This is the setting description",
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 2 (no description)",
                description = null,
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 3",
                description =
                    "This is the setting description that is so long that it doesn't" +
                        "fit on the screen",
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 1",
                description = "This is the setting description",
            ),
            PreviewEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                header = "Entry 2 (no description)",
                description = null,
            ),
        )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        musekitHeader("General")

        musekitItems(entries) { _, item ->
            ListEntry(
                icon = item.icon,
                header = item.header,
                description = item.description,
                onClick = {},
            )
        }
    }
}
