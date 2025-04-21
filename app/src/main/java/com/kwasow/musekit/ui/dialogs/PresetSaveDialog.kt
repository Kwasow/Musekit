package com.kwasow.musekit.ui.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kwasow.musekit.R

// ====== Public composables
@Composable
fun PresetSaveDialog(
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        title = { AlertTitle() },
        text = {
            AlertContent(
                value = name,
                onValueChange = { name = it },
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            AlertConfirmButton(
                enabled = name.isNotBlank(),
                onSet = {
                    onSave(name)
                    onDismiss()
                },
            )
        },
        dismissButton = {
            AlertCancelButton(onDismiss = onDismiss)
        },
    )
}

// ====== Private composables
@Composable
private fun AlertTitle() {
    Text(text = stringResource(id = R.string.save_preset))
}

@Composable
private fun AlertContent(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = stringResource(id = R.string.preset_name)) },
        isError = value.isBlank(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )
}

@Composable
private fun AlertConfirmButton(
    enabled: Boolean,
    onSet: () -> Unit,
) {
    TextButton(
        onClick = onSet,
        enabled = enabled,
    ) {
        Text(text = stringResource(id = R.string.save))
    }
}

@Composable
private fun AlertCancelButton(onDismiss: () -> Unit) {
    TextButton(onClick = onDismiss) {
        Text(text = stringResource(id = R.string.cancel))
    }
}
