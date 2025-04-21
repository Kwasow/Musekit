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
import androidx.constraintlayout.utils.widget.MotionLabel
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.ui.components.AutoSizeText
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun NoteForkManualScreen() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PresetPicker()
        PitchPicker()
        PlayPause(scope = this)
        AdditionalActions()
    }
}

// ====== Private methods
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetPicker() {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = "test",
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
                val fontWeight = FontWeight.Normal
//                    if (sound == selectedSound.value) {
//                        FontWeight.Bold
//                    } else {
//                        FontWeight.Normal
//                    }

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
                    },
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
            onIncrease = {},
            onDecrease = {},
            modifier = Modifier.weight(1f)
        )

        PropertyCard(
            label = stringResource(id = R.string.pitch),
            value = stringResource(id = R.string.pitch_placeholder, viewModel.getPitch()),
            onIncrease = {},
            onDecrease = {},
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
        Box(modifier = androidx.compose.ui.Modifier.weight(1f)) {
            Card(
                modifier =
                    androidx.compose.ui.Modifier
                        .size(50.dp)
                        .clickable {},
            ) {
                Text("Playing")
            }
        }
    }
}

@Composable
private fun AdditionalActions() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = {},
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
