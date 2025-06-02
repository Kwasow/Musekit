package com.kwasow.musekit.ui.dialogs

import androidx.annotation.RawRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.LicenseDialogInfo
import kotlinx.coroutines.launch

// ====== Public composables
@Composable
fun LicenseDialog(
    details: LicenseDialogInfo,
    openFile: suspend (Int) -> String,
) {
    val coroutineScope = rememberCoroutineScope()
    val onDismissRequest = { details.state = LicenseDialogInfo.State.CLOSED }

    if (details.state != LicenseDialogInfo.State.CLOSED) {
        AlertDialog(
            title = { AlertTitle(details = details) },
            text = {
                AlertContent(
                    details = details,
                    openSubDialog = { name, file ->
                        coroutineScope.launch {
                            details.name = name
                            details.state = LicenseDialogInfo.State.LOADING
                            details.text = openFile(file)
                            details.state = LicenseDialogInfo.State.SUBDIALOG_OPEN
                        }
                    },
                )
            },
            onDismissRequest = onDismissRequest,
            confirmButton = { AlertConfirmButton(onClick = onDismissRequest) },
            modifier = Modifier.padding(vertical = 64.dp),
        )
    }
}

@Composable
private fun AlertTitle(details: LicenseDialogInfo) {
    if (details.name.isNotEmpty()) {
        Text(text = details.name)
    } else {
        Text(text = stringResource(id = R.string.licenses))
    }
}

@Composable
private fun AlertContent(
    details: LicenseDialogInfo,
    openSubDialog: (String, Int) -> Unit,
) {
    when (details.state) {
        LicenseDialogInfo.State.CLOSED -> {}
        LicenseDialogInfo.State.DIALOG_OPEN -> {
            LicenseList(openSubDialog = openSubDialog)
        }
        LicenseDialogInfo.State.SUBDIALOG_OPEN -> {
            Text(
                text = details.text,
                modifier = Modifier.verticalScroll(rememberScrollState(0)),
            )
        }
        LicenseDialogInfo.State.LOADING -> {
            Text("TODO: Loading")
        }
    }
}

@Composable
private fun LicenseList(openSubDialog: (String, Int) -> Unit) {
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
