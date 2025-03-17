package com.kwasow.musekit.managers

import android.content.Context
import com.kwasow.musekit.data.Preset

interface PresetsManager {
    // ====== Methods
    fun savePreset(preset: Preset)

    fun getPresets(): List<Preset>

    fun removePreset(name: String)
}
