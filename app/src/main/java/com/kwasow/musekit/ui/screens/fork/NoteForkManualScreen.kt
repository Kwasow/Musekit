package com.kwasow.musekit.ui.screens.fork

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.components.AutoSizeText
import com.kwasow.musekit.ui.components.PlayPauseButton
import com.kwasow.musekit.ui.dialogs.PresetRemoveDialog
import com.kwasow.musekit.ui.dialogs.PresetSaveDialog
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun NoteForkManualScreen() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PresetPicker()
        PitchPicker()
        PlayPause(scope = this)
        AdditionalActions()
    }

    when (viewModel.currentDialog) {
        NoteForkScreenViewModel.Dialog.SAVE_PRESET -> PresetSaveDialog(
            onSave = { viewModel.addPreset(it, viewModel.currentNote) },
            onDismiss = { viewModel.closeDialog() },
        )
        NoteForkScreenViewModel.Dialog.REMOVE_PRESET -> PresetRemoveDialog(
            name = viewModel.currentRemovePresetName,
            onRemove = { viewModel.confirmRemovePreset() },
            onDismiss = { viewModel.closeDialog() },
        )
        else -> {}
    }
}

// ====== Private methods
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetPicker() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = viewModel.currentPreset,
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.presets)) },
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
            viewModel.presets.forEach { (name, note) ->
                val fontWeight =
                    if (name == viewModel.currentPreset) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }

                DropdownMenuItem(
                    text = {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = fontWeight,
                        )
                    },
                    onClick = {
                        expanded = false
                        viewModel.currentPreset = name
                        viewModel.setNote(note)
                    },
                    trailingIcon = {
                        if (name != viewModel.defaultPreset.first) {
                            IconButton(onClick = {
                                viewModel.removePreset(name)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PitchPicker() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()

    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PropertyCard(
            label = stringResource(id = R.string.note),
            value = viewModel.getSuperscriptedNote(viewModel.currentNote),
            onIncrease = {
                viewModel.currentNote = viewModel.currentNote.apply { up() }
            },
            onDecrease = {
                viewModel.currentNote = viewModel.currentNote.apply { down() }
            },
            modifier = Modifier.weight(1f)
        )

        PropertyCard(
            label = stringResource(id = R.string.pitch),
            value = stringResource(id = R.string.pitch_placeholder, viewModel.currentNote.pitch),
            onIncrease = {
                viewModel.currentNote = viewModel.currentNote.apply { pitch += 1 }
            },
            onDecrease = {
                viewModel.currentNote = viewModel.currentNote.apply { pitch -= 1}
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PropertyCard(
    label: String,
    value: CharSequence,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
            )

            AutoSizeText(
                text = value,
                boldFont = false,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onDecrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus_circle),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                IconButton(onClick = onIncrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus_circle),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayPause(scope: ColumnScope) {
    with(scope) {
        PlayPauseButton(
            modifier = Modifier.weight(1f),
            isPlaying = false,
            onChange = { },
        )
    }
}

@Composable
private fun AdditionalActions() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = {
                viewModel.currentDialog = NoteForkScreenViewModel.Dialog.SAVE_PRESET
            },
            contentPadding = PaddingValues(16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = "",
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = R.string.save_preset))
        }
    }
}
