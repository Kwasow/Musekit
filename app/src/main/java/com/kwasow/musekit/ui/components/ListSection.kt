package com.kwasow.musekit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.extensions.itemsWithDividers

// ====== Public composables
@Composable
fun <T> ListSection(
    title: String = "",
    items: List<T>,
    columns: GridCells = GridCells.Fixed(1),
    state: LazyGridState = rememberLazyGridState(),
    itemContent: @Composable (LazyGridItemScope.(index: Int, item: T) -> Unit),
) {
    val color = MaterialTheme.colorScheme.surfaceContainerHigh

    Column(modifier = Modifier.padding(16.dp)) {
        if (title.isNotEmpty()) {
            SectionTitle(title = title)
        }

        Card(colors = CardDefaults.cardColors(color)) {
            LazyVerticalGrid(columns = columns) {
                itemsWithDividers(
                    items = items,
                    state = state,
                    itemContent = itemContent,
                )
            }
        }
    }
}

// ===== Private composables
@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
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

    ListSection(
        title = "General",
        items = entries,
    ) { _, (icon, header, description) ->
        ListEntry(
            icon = icon,
            header = header,
            description = description,
            onClick = {},
        )
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

    ListSection(
        title = "General",
        items = entries,
        columns = GridCells.Fixed(2),
    ) { _, (icon, header, description) ->
        ListEntry(
            icon = icon,
            header = header,
            description = description,
            onClick = {},
        )
    }
}
