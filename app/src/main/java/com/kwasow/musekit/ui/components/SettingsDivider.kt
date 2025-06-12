package com.kwasow.musekit.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// ====== Public composables
@Composable
fun SettingsDivider() {
    HorizontalDivider(
        thickness = 3.dp,
        color = MaterialTheme.colorScheme.background,
    )
}
