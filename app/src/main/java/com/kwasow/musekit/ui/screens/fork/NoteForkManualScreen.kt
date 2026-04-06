package com.kwasow.musekit.ui.screens.fork

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.dialogs.RemovePresetDialog
import com.kwasow.musekit.room.Preset
import com.kwasow.musekit.ui.components.AutoSizeText
import com.kwasow.musekit.ui.components.PlayPauseButton
import com.kwasow.musekit.ui.dialogs.PresetRemoveDialog
import com.kwasow.musekit.ui.dialogs.PresetSaveDialog
import com.kwasow.musekit.utils.ScreenUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun NoteForkManualScreen() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    var presetDialog by remember { mutableStateOf(RemovePresetDialog()) }
    var currentPreset: Preset? by remember { mutableStateOf(viewModel.defaultPreset) }

    val coroutineScope = rememberCoroutineScope()

    if (ScreenUtils.isWide()) {
        WideView(
            currentPreset = currentPreset,
            setPreset = { preset ->
                currentPreset = preset
                viewModel.setNoteFromPreset(preset)
            },
            onSavePreset = { presetDialog.openSave() },
            onRemovePreset = { presetDialog.openRemove(it) },
        )
    } else {
        DefaultView(
            currentPreset = currentPreset,
            setPreset = { preset ->
                currentPreset = preset
                viewModel.setNoteFromPreset(preset)
            },
            onSavePreset = { presetDialog.openSave() },
            onRemovePreset = { presetDialog.openRemove(it) },
        )
    }

    when (presetDialog.state) {
        RemovePresetDialog.State.SAVE_PRESET ->
            PresetSaveDialog(
                onSave = { name ->
                    coroutineScope.launch {
                        val newPreset = viewModel.addPreset(name, viewModel.currentNote)
                        currentPreset = newPreset
                        presetDialog.close()
                    }
                },
                onDismiss = { presetDialog.close() },
            )
        RemovePresetDialog.State.REMOVE_PRESET ->
            PresetRemoveDialog(
                name = presetDialog.removePreset?.name ?: "null",
                onRemove = {
                    viewModel.removePreset(presetDialog.removePreset?.id ?: 0)
                    currentPreset = null
                    presetDialog.close()
                },
                onDismiss = { presetDialog.close() },
            )
        else -> {}
    }
}

// ====== Private methods
@Composable
private fun DefaultView(
    currentPreset: Preset?,
    setPreset: (Preset) -> Unit,
    onSavePreset: () -> Unit,
    onRemovePreset: (Preset) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PresetPicker(
            currentPreset = currentPreset,
            setPreset = setPreset,
            onRemovePreset = onRemovePreset,
            modifier = Modifier.fillMaxWidth(),
        )
        PitchPicker()
        PlayPause(modifier = Modifier.weight(1f))
        AdditionalActions(
            onSavePreset = onSavePreset,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun WideView(
    currentPreset: Preset?,
    setPreset: (Preset) -> Unit,
    onSavePreset: () -> Unit,
    onRemovePreset: (Preset) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PresetPicker(
                currentPreset = currentPreset,
                setPreset = setPreset,
                onRemovePreset = onRemovePreset,
                modifier = Modifier.weight(1f),
            )
            AdditionalActions(
                onSavePreset = onSavePreset,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PitchPickerCards(modifier = Modifier.weight(1f))
            PlayPause()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetPicker(
    currentPreset: Preset?,
    setPreset: (Preset) -> Unit,
    onRemovePreset: (Preset) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val presets = viewModel.presets.observeAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = currentPreset?.name ?: "—",
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.presets)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            modifier =
                Modifier
                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            presets.value?.forEach { preset ->
                val fontWeight =
                    if (preset.id == currentPreset?.id) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }

                DropdownMenuItem(
                    text = {
                        Text(
                            text = preset.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = fontWeight,
                        )
                    },
                    onClick = {
                        expanded = false
                        setPreset(preset)
                    },
                    trailingIcon = {
                        if (preset.id != viewModel.defaultPreset.id) {
                            IconButton(onClick = { onRemovePreset(preset) }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription =
                                        stringResource(
                                            id = R.string.contentDescription_trash_can,
                                        ),
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
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PitchPickerCards(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PitchPickerCards(modifier: Modifier) {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val notationStyle by viewModel.notationStyle.collectAsState()

    PropertyCard(
        label = stringResource(id = R.string.note),
        value = viewModel.getSuperscriptedNote(viewModel.currentNote, notationStyle),
        onIncrease = {
            viewModel.setNote(viewModel.currentNote.apply { up() })
        },
        onDecrease = {
            viewModel.setNote(viewModel.currentNote.apply { down() })
        },
        modifier = modifier,
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
        modifier = modifier,
    )
}

@Composable
private fun PropertyCard(
    label: String,
    value: AnnotatedString,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier,
) {
    val pad = 16.dp

    Card(modifier = modifier) {
        val textModifier =
            if (ScreenUtils.isWide()) {
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            } else {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            }

        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = pad, end = pad, top = pad),
        )

        AutoSizeText(
            text = value,
            boldFont = false,
            modifier = textModifier.padding(horizontal = pad),
        )

        Row(
            modifier =
                Modifier
                    .padding(start = pad, end = pad, bottom = pad)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onDecrease) {
                Icon(
                    imageVector = Icons.Outlined.RemoveCircleOutline,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_decrease,
                        ),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            IconButton(onClick = onIncrease) {
                Icon(
                    imageVector = Icons.Outlined.AddCircleOutline,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_increase,
                        ),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun PlayPause(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val isPlaying = viewModel.isPlaying.observeAsState(false)

    DisposableEffect(true) {
        onDispose {
            viewModel.stopNote()
        }
    }

    PlayPauseButton(
        modifier = modifier,
        isPlaying = isPlaying.value,
        onChange = { viewModel.startStopNote() },
    )
}

@Composable
private fun AdditionalActions(
    onSavePreset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Button(
            onClick = onSavePreset,
            contentPadding = PaddingValues(16.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Save,
                contentDescription = stringResource(id = R.string.contentDescription_save),
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = R.string.save_preset))
        }
    }
}
