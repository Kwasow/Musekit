package com.kwasow.musekit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R

// ====== Public composables
@Composable
fun TunerView(
    note: AnnotatedString?,
    cents: Double?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(id = R.string.play_sound_to_tune))

        TuningStateIndicator(
            note = note,
            cents = cents,
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_check_circle),
            contentDescription = stringResource(id = R.string.contentDescription_tuner_check),
            modifier = Modifier.size(48.dp),
            tint =
                when (cents) {
                    null -> MaterialTheme.colorScheme.surfaceContainer
                    in -5.0..5.0 -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.surfaceContainer
                },
        )
    }
}

// ======= Private composables
@Composable
private fun TuningStateIndicator(
    note: AnnotatedString?,
    cents: Double?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Bar(
            height = 30.dp,
            selected = cents != null && -50 <= cents && cents < -41,
        )
        Bar(
            height = 45.dp,
            selected = cents != null && -41 <= cents && cents < -32,
        )
        Bar(
            height = 60.dp,
            selected = cents != null && -32 <= cents && cents < -23,
        )
        Bar(
            height = 75.dp,
            selected = cents != null && -23 <= cents && cents < -14,
        )
        Bar(
            height = 90.dp,
            selected = cents != null && -14 <= cents && cents < -5,
        )

        AutoSizeText(
            text = note ?: AnnotatedString("—"),
            modifier =
                Modifier
                    .padding(12.dp)
                    .size(90.dp),
        )

        Bar(
            height = 90.dp,
            selected = cents != null && 5 < cents && cents <= 14,
        )
        Bar(
            height = 75.dp,
            selected = cents != null && 14 < cents && cents <= 23,
        )
        Bar(
            height = 60.dp,
            selected = cents != null && 23 < cents && cents <= 32,
        )
        Bar(
            height = 45.dp,
            selected = cents != null && 32 < cents && cents <= 41,
        )
        Bar(
            height = 30.dp,
            selected = cents != null && 41 < cents && cents <= 50,
        )
    }
}

@Composable
private fun Bar(
    height: Dp,
    selected: Boolean,
) {
    Card(
        modifier =
            Modifier
                .padding(4.dp)
                .width(8.dp)
                .height(height),
        content = {},
        colors =
            CardDefaults.cardColors(
                containerColor =
                    when (selected) {
                        true -> MaterialTheme.colorScheme.primary
                        false -> MaterialTheme.colorScheme.surfaceContainer
                    },
            ),
    )
}
