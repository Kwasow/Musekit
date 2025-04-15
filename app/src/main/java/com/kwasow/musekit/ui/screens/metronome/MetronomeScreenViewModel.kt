package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.managers.PreferencesManager

class MetronomeScreenViewModel(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Fields
    var selectedSound by mutableStateOf(MetronomeSounds.Default)

    // ====== Public methods
    fun getBpm() = preferencesManager.getMetronomeBPM()

    fun updateSound(sound: MetronomeSounds) {
        selectedSound = sound
    }
}
