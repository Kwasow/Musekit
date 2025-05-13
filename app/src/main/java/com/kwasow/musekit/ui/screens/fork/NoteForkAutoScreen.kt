package com.kwasow.musekit.ui.screens.fork

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kwasow.musekit.R
import com.kwasow.musekit.ui.components.PermissionMicrophoneView
import com.kwasow.musekit.ui.components.TunerView
import com.kwasow.musekit.utils.MusekitPitchDetector
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@Composable
fun NoteForkAutoScreen() {
    PermissionMicrophoneView { granted ->
        if (granted) {
            MainView()
        } else {
            MicrophonePermissionNotGranted()
        }
    }
}

// ====== Private composables
@Composable
private fun MainView() {
    val viewModel = koinViewModel<NoteForkScreenViewModel>()
    val pitch by viewModel.automaticTunerPitch.collectAsState(440)
    val style by viewModel.notationStyle.collectAsState(null)

    val dispatcher = remember { MusekitPitchDetector.buildDefaultDispatcher() }
    val pitchDetector = remember { MusekitPitchDetector(dispatcher) { pitch } }
    val detectionResult = pitchDetector.detectionResult.observeAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        pitchDetector.startListening()

        onDispose {
            pitchDetector.stopListening()
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        TunerView(
            note =
                style?.let { style ->
                    val note = detectionResult.value?.first ?: return@let null
                    return@let viewModel.getSuperscriptedNote(note, style)
                },
            cents = detectionResult.value?.second,
        )
    }
}

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
