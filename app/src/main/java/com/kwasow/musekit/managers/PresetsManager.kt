package com.kwasow.musekit.managers

import com.kwasow.musekit.room.Preset

interface PresetsManager {
    // ====== Methods
    suspend fun savePreset(preset: Preset)

    suspend fun getPresets(): List<Preset>

    suspend fun removePreset(id: Long)
}
