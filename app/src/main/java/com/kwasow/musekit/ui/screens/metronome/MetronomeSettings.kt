package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun MetronomeSettings() {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        TempoSetting()
    }
}

// ====== Private composables
@Composable
fun TempoSetting() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val currentTempo by viewModel.metronomeBpm.collectAsState()

    Text(
        text = stringResource(id = R.string.beat),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        intArrayOf(-5, -2, -1, 1, 2, 5).forEach { value ->
            OutlinedButton(
                onClick = { currentTempo?.let { viewModel.setBpm(it + value) } },
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                val text = if (value > 0) {
                    "+$value"
                } else {
                    value.toString()
                }

                Text(text)
            }
        }
    }
}
