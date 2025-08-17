package com.kwasow.musekit.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R

// ====== Public composables
@Composable
fun SetBeatDialog(
    initialValue: Int,
    onDismiss: () -> Unit,
    onSet: (Int) -> Unit,
) {
    var beat by remember { mutableStateOf(initialValue.toString()) }

    AlertDialog(
        title = { AlertTitle() },
        text = {
            AlertContent(
                value = beat,
                onValueChange = { beat = it },
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            AlertConfirmButton(
                enabled = beat.isNotBlank(),
                onSet = {
                    onSet(beat.toInt())
                    onDismiss()
                },
            )
        },
        dismissButton = { AlertCancelButton(onDismiss = onDismiss) },
    )
}

// ====== Private composables
@Composable
private fun AlertTitle() {
    Text(text = stringResource(id = R.string.set_tempo))
}

@Composable
private fun AlertContent(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "60",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            isError = value.isBlank(),
            modifier = Modifier.width(64.dp),
            singleLine = true,
            textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center),
        )
    }
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
