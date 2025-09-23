package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwasow.musekit.R
import com.kwasow.musekit.extensions.allowSleep
import com.kwasow.musekit.extensions.preventSleep
import com.kwasow.musekit.services.MetronomeService
import com.kwasow.musekit.ui.components.PlayPauseButton
import com.kwasow.musekit.ui.components.rememberBoundLocalService
import com.kwasow.musekit.ui.dialogs.SetBeatDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeScreen() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

    val context = LocalContext.current
    val metronomeService =
        rememberBoundLocalService<MetronomeService, MetronomeService.LocalBinder> { service }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val bpm by viewModel.metronomeBpm.collectAsState()
    var showSetBeatDialog by remember { mutableStateOf(false) }

    val tickListener = remember {
        object : MetronomeService.TickListener {
            override fun onStart() {
                viewModel.isPlaying.postValue(true)
            }

            override fun onTick(index: Int) {
                viewModel.currentBeat.postValue(index)
            }

            override fun onStop() {
                viewModel.isPlaying.postValue(false)
            }
        }
    }

    DisposableEffect(Unit) {
        context.preventSleep()
        metronomeService?.listener = tickListener

        onDispose {
            context.allowSleep()
            metronomeService?.listener = null
        }
    }

    DisposableEffect(metronomeService) {
        metronomeService?.listener = tickListener

        onDispose {
            metronomeService?.listener = null
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            MetronomeSettings(onOpenSetBeatDialog = { showSetBeatDialog = true })
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 148.dp,
    ) {
        MainView(
            service = metronomeService,
            closeBottomSheet = {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.partialExpand()
                }
            },
        )

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
private fun MainView(
    service: MetronomeService?,
    closeBottomSheet: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        TempoDisplay(modifier = Modifier.align(Alignment.TopEnd))

        Column(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BeatIndicator()
            PlayPause(
                service = service,
                closeBottomSheet = closeBottomSheet,
            )
        }
    }
}

@Composable
private fun TempoDisplay(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()
    val currentTempo by viewModel.metronomeBpm.collectAsState()

    val text =
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("♪ ${stringResource(id = R.string.tempo)} = ")
            }
            append(currentTempo?.toString() ?: "")
        }

    Text(
        text = text,
        fontSize = 20.sp,
        modifier = modifier,
    )
}

@Composable
private fun BeatIndicator() {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

    val totalBeats by viewModel.metronomeNumberOfBeats.collectAsState()
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val currentBeat by viewModel.currentBeat.observeAsState(0)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        totalBeats?.let { beats ->
            (0 until beats).forEach { index ->
                val active = isPlaying && currentBeat == index
                val color =
                    if (active) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceContainer
                    }

                Canvas(
                    modifier =
                        Modifier
                            .size(30.dp)
                            .padding(2.dp),
                ) {
                    drawCircle(color = color)
                }
            }
        }
    }
}

@Composable
private fun PlayPause(
    service: MetronomeService?,
    closeBottomSheet: () -> Unit,
) {
    val viewModel = koinViewModel<MetronomeScreenViewModel>()

    val isPlaying by viewModel.isPlaying.observeAsState(false)

    PlayPauseButton(
        isPlaying = isPlaying,
        onChange = {
            service?.startStopMetronome()

            if (!isPlaying) {
                closeBottomSheet()
            }
        },
    )
}
