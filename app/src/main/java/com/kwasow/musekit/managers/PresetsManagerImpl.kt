package com.kwasow.musekit.managers

import com.kwasow.musekit.room.AppDatabase
import com.kwasow.musekit.room.Preset

class PresetsManagerImpl(
    private val database: AppDatabase,
) : PresetsManager {
    // ====== Fields
    private val presetDao = database.presetDao()

    // ====== Public methods
    override suspend fun savePreset(preset: Preset): Long? {
        // TODO: Someone could name their preset "Default"
        if (preset.name.isNotBlank()) {
            return presetDao.insert(preset)
        }

        return null
    }

    override suspend fun getPresets(): List<Preset> = presetDao.getAll()

    override suspend fun removePreset(id: Long) = presetDao.deleteById(id)
}
