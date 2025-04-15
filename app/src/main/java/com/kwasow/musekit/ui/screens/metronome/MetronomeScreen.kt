package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import org.koin.androidx.compose.koinViewModel

// ======= Public composables
@Composable
fun MetronomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SoundPicker()
        TempoPicker()
        BeatIndicator()
        StartStopButton()
        AdditionalActions()
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoundPicker() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        OutlinedTextField(
            value = stringResource(id = viewModel.selectedSound.resourceNameId),
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.sound)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
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
                        viewModel.updateSound(sound)
                    }
                )
            }
        }
    }
}

@Composable
private fun TempoPicker() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DecreaseButtons()

        Text(
            text = viewModel.currentTempo.toString(),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        )

        IncreaseButtons()
    }
}

@Composable
private fun DecreaseButtons() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        OutlinedButton(
            onClick = { viewModel.updateTempoBy(-5) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.minus5),
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        OutlinedButton(
            onClick = { viewModel.updateTempoBy(-2) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.minus2),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        OutlinedButton(
            onClick = { viewModel.updateTempoBy(-1) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.minus1),
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun IncreaseButtons() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        OutlinedButton(
            onClick = { viewModel.updateTempoBy(5) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.plus5),
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        OutlinedButton(
            onClick = { viewModel.updateTempoBy(2) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.plus2),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        OutlinedButton(
            onClick = { viewModel.updateTempoBy(1) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.plus1),
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun BeatIndicator() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Slider(
        value = sliderPosition,
        steps = 0,
        valueRange = 0f..1f,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        enabled = false,
        interactionSource = remember { MutableInteractionSource() }
    )
}

@Composable
private fun StartStopButton() {

}

@Composable
private fun AdditionalActions() {

}
