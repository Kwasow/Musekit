package com.kwasow.musekit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R

// ====== Public composables
@Composable
fun TunerView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(id = R.string.play_sound_to_tune))

        TuningStateIndicator()

        Icon(
            painter = painterResource(id = R.drawable.ic_check_circle),
            contentDescription = stringResource(id = R.string.contentDescription_tuner_check),
            modifier = Modifier.size(48.dp),
        )
    }
}

// ======= Private composables
@Composable
private fun TuningStateIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Bar(height = 30.dp)
        Bar(height = 45.dp)
        Bar(height = 60.dp)
        Bar(height = 75.dp)
        Bar(height = 90.dp)

        AutoSizeText(
            text = "A4",
            modifier =
                Modifier
                    .padding(12.dp)
                    .size(90.dp),
        )

        Bar(height = 90.dp)
        Bar(height = 75.dp)
        Bar(height = 60.dp)
        Bar(height = 45.dp)
        Bar(height = 30.dp)
    }
}

@Composable
private fun Bar(height: Dp) {
    Card(
        modifier =
            Modifier
                .padding(4.dp)
                .width(8.dp)
                .height(height),
        content = {},
    )
}
