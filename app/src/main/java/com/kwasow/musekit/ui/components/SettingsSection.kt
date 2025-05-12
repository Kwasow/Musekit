package com.kwasow.musekit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ====== Public composables
@Composable
fun SettingsSection(
    title: String = "",
    entries: @Composable ColumnScope.() -> Unit,
) {
    val color = MaterialTheme.colorScheme.onPrimary

    Column(modifier = Modifier.padding(16.dp)) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(color),
        ) {
            entries()
        }
    }
}

// ====== Previews
@Preview
@Composable
private fun SettingsSectionPreview() {
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) {
        SettingsSection(title = "General") {
            SettingsEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                name = "Entry 1",
                description = "This is the setting description",
                onClick = {},
            )
            HorizontalDivider()
            SettingsEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                name = "Entry 2 (no description)",
                onClick = {},
            )
            HorizontalDivider()
            SettingsEntry(
                icon = rememberVectorPainter(image = Icons.Outlined.Add),
                name = "Entry 3",
                description =
                    "This is the setting description that is so long that it doesn't" +
                        "fit on the screen",
                onClick = {},
            )
        }
    }
}
