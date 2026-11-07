package com.kwasow.musekit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ====== Public composables
@Composable
fun ListSection(
    title: String? = null,
    entries: List<@Composable ColumnScope.() -> Unit>,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        title?.let { SectionTitle(title = it) }

        entries.forEachIndexed { index, item ->
            val shape =
                MusekitUIHelpers.gridItemShape(
                    index = index,
                    totalItems = entries.size,
                    columns = 1,
                )

            val padding = if (entries.lastIndex != index) 4.dp else 0.dp

            Column(
                modifier =
                    Modifier
                        .padding(bottom = padding)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                item()
            }
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun ListSectionPreview() {
    ListSection(
        title = "General",
        entries =
            listOf(
                {
                    ListEntry(
                        icon = rememberVectorPainter(image = Icons.Outlined.Add),
                        header = "Entry 1",
                        description = "This is the setting description",
                        onClick = {},
                    )
                },
                {
                    ListEntry(
                        icon = rememberVectorPainter(image = Icons.Outlined.Add),
                        header = "Entry 2 (no description)",
                        onClick = {},
                    )
                },
                {
                    ListEntry(
                        icon = rememberVectorPainter(image = Icons.Outlined.Add),
                        header = "Entry 3",
                        description =
                            "This is the setting description that is so long that it doesn't" +
                                "fit on the screen",
                        onClick = {},
                    )
                },
            ),
    )
}
