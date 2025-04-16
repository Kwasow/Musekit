package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.services.MetronomeService
import com.kwasow.musekit.ui.components.rememberBoundLocalService
import org.koin.androidx.compose.koinViewModel

// ======= Public composables
@Composable
fun MetronomeScreen() {
    val metronomeService =
        rememberBoundLocalService<MetronomeService, MetronomeService.LocalBinder> { service }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (metronomeService != null) {
            SoundPicker(service = metronomeService)
            TempoPicker(service = metronomeService)
            BeatIndicator(service = metronomeService)
            StartStopButton(this)
            AdditionalActions()
        } else {
            Text("TODO: Error/loading")
        }
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoundPicker(service: MetronomeService) {
    var expanded by remember { mutableStateOf(false) }
    val selectedSound = service.sound.observeAsState()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
    ) {
        OutlinedTextField(
            value = stringResource(id = selectedSound.value!!.resourceNameId),
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
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = sound.resourceNameId),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = {
                        expanded = false
                        service.setSound(sound)
                    },
                )
            }
        }
    }
}

@Composable
private fun TempoPicker(service: MetronomeService) {
    val currentTempo = service.bpm.observeAsState()
    val onChange = { by: Int ->
        service.updateTempo(by)
    }

    var textStyle by remember {
        mutableStateOf(
            TextStyle(
                fontSize = 300.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )
    }
    var readyToDraw by remember { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChangeButtons(
            values = listOf(-5, -2, -1),
            onChange = onChange,
        )

        Text(
            text = currentTempo.value.toString(),
            style = textStyle,
            maxLines = 1,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .drawWithContent {
                        if (readyToDraw) drawContent()
                    },
            onTextLayout = {
                if (it.hasVisualOverflow) {
                    textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.80)
                } else {
                    readyToDraw = true
                }
            },
        )

        ChangeButtons(
            values = listOf(5, 2, 1),
            onChange = onChange,
        )
    }
}

@Composable
private fun ChangeButtons(
    values: List<Int>,
    onChange: (Int) -> Unit
) {
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        values.forEach { value ->
            OutlinedButton(
                onClick = { onChange(value) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (value > 0) "+$value" else value.toString(),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun BeatIndicator(service: MetronomeService) {
    val sliderPosition = service.tickerPosition.observeAsState()

    Slider(
        value = sliderPosition.value!!,
        steps = 0,
        valueRange = 0f..1f,
        onValueChange = {},
        modifier =
            Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
        enabled = false,
        interactionSource = remember { MutableInteractionSource() },
    )
}

@Composable
private fun StartStopButton(
    service: MetronomeService,
    scope: ColumnScope,
) {
    val isPlaying = service.isPlaying.observeAsState()

    with(scope) {
        Box(modifier = Modifier.weight(1f)) {
            Card(
                modifier =
                    Modifier
                        .size(50.dp)
                        .clickable { service.startStopMetronome() },
            ) {
                if (isPlaying.value == true) {
                    Text("Playing")
                } else {
                    Text("Paused")
                }
            }
        }
    }
}

@Composable
private fun AdditionalActions() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = {},
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
            onClick = {},
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
