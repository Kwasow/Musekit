package com.kwasow.musekit.models

import androidx.lifecycle.ViewModel
import com.kwasow.musekit.data.Preset
import com.kwasow.musekit.managers.PresetsManager

class PresetDialogViewModel(
    private val presetsManager: PresetsManager,
) : ViewModel() {
    // ======= Public methods
    fun removePreset(name: String) = presetsManager.removePreset(name)

    fun addPreset(preset: Preset) = presetsManager.savePreset(preset)
}
