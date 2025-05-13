package com.kwasow.musekit.ui.screens.fork

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.components.PermissionMicrophoneView
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun NoteForkAutoScreen() {
    PermissionMicrophoneView { granted ->
        if (granted) {
            Text("Auto")
        } else {
            MicrophonePermissionNotGranted()
        }
    }
}

// ====== Private composables
@Composable
private fun MicrophonePermissionNotGranted() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            stringResource(R.string.record_permission_not_granted),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 36.dp),
        )

        Button(
            onClick = { viewModel.launchPermissionSettings() },
        ) {
            Text(stringResource(R.string.settings))
        }
    }
}
