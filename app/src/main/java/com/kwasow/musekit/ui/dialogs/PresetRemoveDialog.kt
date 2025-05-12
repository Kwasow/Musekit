package com.kwasow.musekit.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kwasow.musekit.R

// ====== Public composables
@Composable
fun PresetRemoveDialog(
    name: String,
    onRemove: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { AlertTitle() },
        text = { AlertContent(name = name) },
        onDismissRequest = onDismiss,
        confirmButton = { AlertConfirmButton(onRemove = onRemove) },
        dismissButton = { AlertCancelButton(onDismiss = onDismiss) },
    )
}

// ====== Private composables
@Composable
private fun AlertTitle() {
    Text(text = stringResource(id = R.string.delete_preset))
}

@Composable
private fun AlertContent(name: String) {
    Text(text = stringResource(id = R.string.warning_preset_deletion, name))
}

@Composable
private fun AlertConfirmButton(onRemove: () -> Unit) {
    TextButton(onClick = onRemove) {
        Text(text = stringResource(id = R.string.delete))
    }
}

@Composable
private fun AlertCancelButton(onDismiss: () -> Unit) {
    TextButton(onClick = onDismiss) {
        Text(text = stringResource(id = R.string.cancel))
    }
}
