package com.kwasow.musekit.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ====== Public composables
@Composable
fun PilledTabRow(
    modifier: Modifier = Modifier,
    tabs: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    RoundedCornerShape(8.dp),
                )
                .height(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs()
        }
    }
}

@Composable
fun RowScope.PilledTabItem(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabTextColor by animateColorAsState(
        targetValue =
            when (isSelected) {
                true -> MaterialTheme.colorScheme.onSecondaryContainer
                false -> MaterialTheme.colorScheme.onBackground
            },
        animationSpec = tween(easing = LinearEasing),
        label = "pilledTab_textColor",
    )

    val background by animateColorAsState(
        targetValue =
            when (isSelected) {
                true -> MaterialTheme.colorScheme.secondaryContainer
                false -> MaterialTheme.colorScheme.surfaceContainer
            },
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "pilledTab_backgroundColor",
    )

    Box(
        modifier =
            modifier
                .weight(1f)
                .fillMaxHeight(1f)
                .background(background, RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = tabTextColor,
        )
    }
}

// ====== Previews
@Preview
@Composable
fun PilledTabsPreview() {
    PilledTabRow {
        PilledTabItem(
            isSelected = true,
            text = "Tab1",
            onClick = {},
        )

        PilledTabItem(
            isSelected = false,
            text = "Tab2",
            onClick = {},
        )
    }
}
