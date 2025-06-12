package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun MetronomeSettings(onOpenSetBeatDialog: () -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        item {
            SectionTitle(
                text = stringResource(id = R.string.beat),
                paddingValues = PaddingValues(bottom = 16.dp),
            )
            TempoSetting(onOpenSetBeatDialog = onOpenSetBeatDialog)
        }

        item {
            SectionTitle(stringResource(id = R.string.sound))
            SoundSettings()
        }
    }
}

// ====== Private composables
@Composable
private fun SectionTitle(
    text: String,
    paddingValues: PaddingValues = PaddingValues(vertical = 16.dp),
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun TempoSetting(onOpenSetBeatDialog: () -> Unit) {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val currentTempo by viewModel.metronomeBpm.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        intArrayOf(-5, -2, -1, 1, 2, 5).forEach { value ->
            OutlinedButton(
                onClick = { currentTempo?.let { viewModel.setBpm(it + value) } },
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
            ) {
                val text =
                    if (value > 0) {
                        "+$value"
                    } else {
                        value.toString()
                    }

                Text(text)
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = onOpenSetBeatDialog,
            contentPadding = PaddingValues(16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "",
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = R.string.set_beat))
        }

        Button(
            onClick = {
                viewModel.beatEvent()?.let { res ->
                    viewModel.setBpm(res)
                }
            },
            contentPadding = PaddingValues(16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tap),
                contentDescription = "",
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = R.string.tap_beat))
        }
    }
}

@Composable
private fun SoundSettings() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val currentSound by viewModel.metronomeSound.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        MetronomeSounds.entries.forEach { sound ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(
                    selected = currentSound == sound,
                    onClick = { viewModel.setSound(sound) },
                )

                Text(stringResource(id = sound.resourceNameId))
            }
        }
    }
}
