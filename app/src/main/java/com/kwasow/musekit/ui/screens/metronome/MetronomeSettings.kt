package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.kwasow.musekit.ui.components.AutoSizeText
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun MetronomeSettings(onOpenSetBeatDialog: () -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        item {
            SectionTitle(
                text = stringResource(id = R.string.tempo),
                paddingValues = PaddingValues(bottom = 16.dp),
            )
            TempoSetting(onOpenSetBeatDialog = onOpenSetBeatDialog)
        }

        item {
            SectionTitle(stringResource(id = R.string.sound))
            SoundSettings()
        }

        item {
            SectionTitle(stringResource(id = R.string.beats))
            BeatsSettings()
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
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        Button(
            onClick = onOpenSetBeatDialog,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.padding(end = 8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.contentDescription_pen),
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = R.string.set_tempo))
        }

        Button(
            onClick = {
                viewModel.beatEvent()?.let { res ->
                    viewModel.setBpm(res)
                }
            },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tap),
                contentDescription = stringResource(id = R.string.contentDescription_finger_tapping),
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = R.string.tap_tempo))
        }
    }
}

@Composable
private fun BeatsSettings() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val numberOfBeats by viewModel.metronomeNumberOfBeats.collectAsState()

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(bottom = 12.dp),
    ) {
        IconButton(
            onClick = { numberOfBeats?.let { viewModel.setNumberOfBeats(it - 1) } },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_minus_circle),
                contentDescription = stringResource(id = R.string.contentDescription_decrease),
                modifier = Modifier.size(28.dp),
            )
        }

        AutoSizeText(
            text = numberOfBeats.toString(),
            modifier = Modifier.fillMaxHeight()
        )

        IconButton(
            onClick = { numberOfBeats?.let { viewModel.setNumberOfBeats(it + 1) } },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus_circle),
                contentDescription = stringResource(id = R.string.contentDescription_increase),
                modifier = Modifier.size(28.dp),
            )
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
