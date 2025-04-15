package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import org.koin.androidx.compose.koinViewModel

// ======= Public composables
@Composable
fun MetronomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SoundPicker()
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
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 32.dp),
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
                    text = { Text(text = stringResource(id = sound.resourceNameId)) },
                    onClick = {
                        expanded = false
                        viewModel.updateSound(sound)
                    }
                )
            }
        }
    }
}
