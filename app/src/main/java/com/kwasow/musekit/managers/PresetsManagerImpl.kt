package com.kwasow.musekit.managers

import com.kwasow.musekit.room.AppDatabase
import com.kwasow.musekit.room.Preset
import kotlinx.coroutines.flow.Flow

class PresetsManagerImpl(
    database: AppDatabase,
) : PresetsManager {
    // ====== Fields
    private val presetDao = database.presetDao()

    // ====== Interface methods
    override val presets: Flow<List<Preset>> = presetDao.getAll()

    override suspend fun savePreset(preset: Preset): Long? {
        // TODO: Someone could name their preset "Default"
        if (preset.name.isNotBlank()) {
            return presetDao.insert(preset)
        }

        return null
    }

    override suspend fun removePreset(id: Long) = presetDao.deleteById(id)
}
