package com.kwasow.musekit.ui.dialogs

import androidx.annotation.RawRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R

// ====== Public composables
@Composable
fun LicenseDialog(
    name: String,
    content: String,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        title = { AlertTitle(title = name) },
        text = {
            Text(
                text = content,
                modifier = Modifier.verticalScroll(rememberScrollState(0)),
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = { AlertConfirmButton(onClick = onDismissRequest) },
        modifier = Modifier.fillMaxHeight(0.75f),
    )
}

@Composable
fun LicensesDialog(
    onDismissRequest: () -> Unit,
    openSubDialog: (String, Int) -> Unit,
) {
    AlertDialog(
        title = { AlertTitle(title = stringResource(id = R.string.licenses)) },
        text = { AlertContent(openSubDialog = openSubDialog) },
        onDismissRequest = onDismissRequest,
        confirmButton = { AlertConfirmButton(onClick = onDismissRequest) },
    )
}

// ====== Private composables
@Composable
private fun AlertTitle(title: String) {
    Text(text = title)
}

@Composable
private fun AlertContent(openSubDialog: (String, Int) -> Unit) {
    Column {
        LicenseEntry(
            name = stringResource(id = R.string.this_app),
            description = stringResource(id = R.string.gpl_3_0),
            file = R.raw.gpl3,
            openSubDialog = openSubDialog,
        )

        LicenseEntry(
            name = stringResource(id = R.string.tarsos),
            description = stringResource(id = R.string.gpl_3_0),
            file = R.raw.gpl3,
            openSubDialog = openSubDialog,
        )

        LicenseEntry(
            name = stringResource(id = R.string.icons),
            description = stringResource(id = R.string.mit),
            file = R.raw.mit,
            openSubDialog = openSubDialog,
        )
    }
}

@Composable
private fun LicenseEntry(
    name: String,
    description: String,
    @RawRes file: Int,
    openSubDialog: (String, Int) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { openSubDialog(name, file) }
                .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Icon(
            painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = stringResource(id = R.string.contentDescription_right_arrow),
        )
    }
}

@Composable
private fun AlertConfirmButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.close))
    }
}
