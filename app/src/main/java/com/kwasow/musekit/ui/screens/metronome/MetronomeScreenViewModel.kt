package com.kwasow.musekit.ui.screens.metronome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.utils.MusekitBeatDetector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MetronomeScreenViewModel(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Fields
    private val beatDetector by lazy { MusekitBeatDetector() }

    val metronomeSound =
        preferencesManager.metronomeSound.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            MetronomeSounds.Default,
        )
    val metronomeBpm =
        preferencesManager.metronomeBpm.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null,
        )
    val metronomeNumberOfBeats =
        preferencesManager.metronomeNumberOfBeats.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null,
        )

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

    fun setNumberOfBeats(value: Int) =
        viewModelScope.launch {
            preferencesManager.setMetronomeNumberOfBeats(value)
        }
}
