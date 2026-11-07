package com.kwasow.musekit.managers

import com.kwasow.musekit.room.Preset
import kotlinx.coroutines.flow.Flow

interface PresetsManager {
    // ====== Fields
    val presets: Flow<List<Preset>>

    // ====== Methods
    suspend fun savePreset(preset: Preset): Long?

    suspend fun removePreset(id: Long)
}
