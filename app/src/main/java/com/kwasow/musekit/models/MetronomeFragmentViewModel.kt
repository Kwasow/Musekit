package com.kwasow.musekit.models

import androidx.lifecycle.ViewModel
import com.kwasow.musekit.managers.PreferencesManager

class MetronomeFragmentViewModel(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Public methods
    fun getBpm() = preferencesManager.getMetronomeBPM()
}
