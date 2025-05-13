package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.utils.MusekitBeatDetector
import kotlinx.coroutines.launch

class MetronomeScreenViewModel(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Fields
    private val beatDetector = MusekitBeatDetector()

    var showSetBeatDialog by mutableStateOf(false)
    val metronomeSound = preferencesManager.metronomeSound
    val metronomeBpm = preferencesManager.metronomeBpm

    // ====== Public methods
    fun beatEvent(): Int? = beatDetector.beatEvent()

    fun setBpm(value: Int) =
        viewModelScope.launch {
            preferencesManager.setMetronomeBpm(value)
        }

    fun setSound(sound: MetronomeSounds) =
        viewModelScope.launch {
            preferencesManager.setMetronomeSound(sound)
        }
}
