package com.kwasow.musekit.ui.screens.fork

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.dialogs.RemovePresetDialog
import com.kwasow.musekit.ui.components.AutoSizeText
import com.kwasow.musekit.ui.components.PlayPauseButton
import com.kwasow.musekit.ui.dialogs.PresetRemoveDialog
import com.kwasow.musekit.ui.dialogs.PresetSaveDialog
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun NoteForkManualScreen() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    var presetDialog by remember { mutableStateOf(RemovePresetDialog()) }
    var currentPreset by remember { mutableStateOf(viewModel.defaultPreset.first) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PresetPicker(
            currentPreset = currentPreset,
            setPreset = { name, note ->
                currentPreset = name
                viewModel.setNote(note)
            },
            onRemovePreset = { presetDialog.openRemove(it) },
        )
        PitchPicker()
        PlayPause(scope = this)
        AdditionalActions(onSavePreset = { presetDialog.openSave() })
    }

    when (presetDialog.state) {
        RemovePresetDialog.State.SAVE_PRESET ->
            PresetSaveDialog(
                onSave = { name ->
                    viewModel.addPreset(name, viewModel.currentNote)
                    currentPreset = name
                    presetDialog.close()
                },
                onDismiss = { presetDialog.close() },
            )
        RemovePresetDialog.State.REMOVE_PRESET ->
            PresetRemoveDialog(
                name = presetDialog.removePresetName,
                onRemove = {
                    viewModel.removePreset(presetDialog.removePresetName)
                    currentPreset = "—"
                    presetDialog.close()
                },
                onDismiss = { presetDialog.close() },
            )
        else -> {}
    }
}

// ====== Private methods
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetPicker(
    currentPreset: String,
    setPreset: (String, Note) -> Unit,
    onRemovePreset: (String) -> Unit,
) {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    var presets = viewModel.presets.observeAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = currentPreset,
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
            presets.value?.forEach { (name, note) ->
                val fontWeight =
                    if (name == currentPreset) {
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
                        setPreset(name, Note(note))
                    },
                    trailingIcon = {
                        if (name != viewModel.defaultPreset.first) {
                            IconButton(onClick = { onRemovePreset(name) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun PitchPicker() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val notationStyle by viewModel.notationStyle.collectAsState()

    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PropertyCard(
            label = stringResource(id = R.string.note),
            value = viewModel.getSuperscriptedNote(viewModel.currentNote, notationStyle),
            onIncrease = {
                viewModel.setNote(viewModel.currentNote.apply { up() })
            },
            onDecrease = {
                viewModel.setNote(viewModel.currentNote.apply { down() })
            },
            modifier = Modifier.weight(1f),
        )

        PropertyCard(
            label = stringResource(id = R.string.pitch),
            value =
                AnnotatedString(
                    stringResource(
                        id = R.string.pitch_placeholder,
                        viewModel.currentNote.pitch,
                    ),
                ),
            onIncrease = {
                viewModel.setNote(viewModel.currentNote.apply { pitch += 1 })
            },
            onDecrease = {
                viewModel.setNote(viewModel.currentNote.apply { pitch -= 1 })
            },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PropertyCard(
    label: String,
    value: AnnotatedString,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier,
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
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val isPlaying = viewModel.isPlaying.observeAsState(false)

    with(scope) {
        PlayPauseButton(
            modifier = Modifier.weight(1f),
            isPlaying = isPlaying.value,
            onChange = { viewModel.startStopNote() },
        )
    }
}

@Composable
private fun AdditionalActions(onSavePreset: () -> Unit) {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = onSavePreset,
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
