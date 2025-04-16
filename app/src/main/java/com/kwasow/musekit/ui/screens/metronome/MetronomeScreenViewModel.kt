package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kwasow.musekit.utils.MusekitBeatDetector

class MetronomeScreenViewModel() : ViewModel() {
    // ====== Fields
    private val beatDetector = MusekitBeatDetector()

    var showSetBeatDialog by mutableStateOf(false)

    // ====== Public methods
    fun beatEvent(): Int? = beatDetector.beatEvent()
}
