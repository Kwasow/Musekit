package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.services.MetronomeService
import com.kwasow.musekit.ui.components.AutoSizeText
import com.kwasow.musekit.ui.components.PlayPauseButton
import com.kwasow.musekit.ui.components.rememberBoundLocalService
import com.kwasow.musekit.ui.dialogs.SetBeatDialog
import org.koin.androidx.compose.koinViewModel

// ======= Public composables
@Composable
fun MetronomeScreen() {
    val metronomeService =
        rememberBoundLocalService<MetronomeService, MetronomeService.LocalBinder> { service }

    MainView(service = metronomeService)
}

// ====== Private composables
@Composable
private fun MainView(service: MetronomeService?) {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    var showSetBeatDialog by remember { mutableStateOf(false) }
    val bpm by viewModel.metronomeBpm.collectAsState(60)

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SoundPicker()
        TempoPicker()
        BeatIndicator(service = service)
        PlayPause(
            service = service,
            scope = this,
        )
        AdditionalActions(onOpenSetBeatDialog = { showSetBeatDialog = true })
    }

    if (showSetBeatDialog) {
        SetBeatDialog(
            initialValue = bpm,
            onDismiss = { showSetBeatDialog = false },
            onSet = { viewModel.setBpm(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoundPicker() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

    var expanded by remember { mutableStateOf(false) }
    val selectedSound by viewModel.metronomeSound.collectAsState(MetronomeSounds.Default)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
    ) {
        val sound = selectedSound

        OutlinedTextField(
            value = stringResource(id = sound.resourceNameId),
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.sound)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            modifier =
                Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            MetronomeSounds.entries.forEach { sound ->
                val fontWeight =
                    if (sound == selectedSound) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }

                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = sound.resourceNameId),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = fontWeight,
                        )
                    },
                    onClick = {
                        expanded = false
                        viewModel.setSound(sound)
                    },
                )
            }
        }
    }
}

@Composable
private fun TempoPicker() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val currentTempo by viewModel.metronomeBpm.collectAsState(null)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChangeButtons(
            values =
                listOf(
                    Pair(-5, 24.sp),
                    Pair(-2, 20.sp),
                    Pair(-1, 16.sp),
                ),
            onChange = { change ->
                currentTempo?.let { tempo -> viewModel.setBpm(tempo + change) }
            },
        )

        AutoSizeText(
            text = currentTempo?.toString() ?: "",
            boldFont = true,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .fillMaxHeight(),
        )

        ChangeButtons(
            values =
                listOf(
                    Pair(5, 24.sp),
                    Pair(2, 20.sp),
                    Pair(1, 16.sp),
                ),
            onChange = { change ->
                currentTempo?.let { tempo -> viewModel.setBpm(tempo + change) }
            },
        )
    }
}

@Composable
private fun ChangeButtons(
    values: List<Pair<Int, TextUnit>>,
    onChange: (Int) -> Unit,
) {
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        values.forEach { (change, size) ->
            OutlinedButton(
                onClick = { onChange(change) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (change > 0) "+$change" else change.toString(),
                    fontSize = size,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun BeatIndicator(service: MetronomeService?) {
    val sliderPosition = service?.tickerPosition?.observeAsState()

    Slider(
        value = sliderPosition?.value ?: 0f,
        steps = 0,
        valueRange = 0f..1f,
        onValueChange = {},
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        enabled = false,
        colors =
            SliderDefaults.colors(
                disabledActiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledInactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledThumbColor = MaterialTheme.colorScheme.primary,
            ),
    )
}

@Composable
private fun PlayPause(
    service: MetronomeService?,
    scope: ColumnScope,
) {
    val isPlaying = service?.isPlaying?.observeAsState()

    with(scope) {
        PlayPauseButton(
            modifier = Modifier.weight(1f),
            isPlaying = isPlaying?.value == true,
            onChange = { service?.startStopMetronome() },
        )
    }
}

@Composable
private fun AdditionalActions(onOpenSetBeatDialog: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        SetBeatButton(onOpenSetBeatDialog = onOpenSetBeatDialog)
        TapBeatButton()
    }
}

@Composable
private fun SetBeatButton(onOpenSetBeatDialog: () -> Unit) {
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
}

@Composable
private fun TapBeatButton() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

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
