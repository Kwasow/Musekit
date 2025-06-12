package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.services.MetronomeService
import com.kwasow.musekit.ui.components.PlayPauseButton
import com.kwasow.musekit.ui.components.rememberBoundLocalService
import com.kwasow.musekit.ui.dialogs.SetBeatDialog
import org.koin.androidx.compose.koinViewModel

// ======= Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeScreen() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val bpm by viewModel.metronomeBpm.collectAsState()

    val metronomeService =
        rememberBoundLocalService<MetronomeService, MetronomeService.LocalBinder> { service }
    var showSetBeatDialog by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        sheetContent = {
            MetronomeSettings(onOpenSetBeatDialog = { showSetBeatDialog = true })
        }
    ) {
        MainView(service = metronomeService)

        if (showSetBeatDialog) {
            SetBeatDialog(
                initialValue = bpm ?: 60,
                onDismiss = { showSetBeatDialog = false },
                onSet = { viewModel.setBpm(it) },
            )
        }
    }
}

// ====== Private composables
@Composable
private fun MainView(service: MetronomeService?) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TempoDisplay()
        BeatIndicator(service = service)
        PlayPause(
            service = service,
            scope = this,
        )
    }
}

@Composable
private fun TempoDisplay() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val currentTempo by viewModel.metronomeBpm.collectAsState()

    Text(
        text = "♪ Tempo = " + (currentTempo?.toString() ?: ""),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun BeatIndicator(service: MetronomeService?) {
    val sliderPosition = service?.tickerPosition?.observeAsState()

    Slider(
        value = sliderPosition?.value ?: 0f,
        steps = 0,
        valueRange = 0f..1f,
        onValueChange = {},
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        enabled = false,
        colors =
            SliderDefaults.colors(
                disabledActiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledInactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledThumbColor = MaterialTheme.colorScheme.primary,
            ),
    )
}

@Composable
private fun PlayPause(
    service: MetronomeService?,
    scope: ColumnScope,
) {
    val isPlaying = service?.isPlaying?.observeAsState()

    with(scope) {
        PlayPauseButton(
            modifier = Modifier.weight(1f),
            isPlaying = isPlaying?.value == true,
            onChange = { service?.startStopMetronome() },
        )
    }
}
