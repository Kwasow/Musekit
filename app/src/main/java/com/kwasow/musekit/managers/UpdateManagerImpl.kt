package com.kwasow.musekit.managers

import android.content.Context
import android.util.Log
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.room.Preset
import com.kwasow.musekit.store.musekitPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.properties.Delegates

class UpdateManagerImpl(
    val context: Context,
    val presetsManager: PresetsManager,
) : UpdateManager {
    // ====== Fields
    private var lastVersionCode by Delegates.notNull<Long>()
    private val currentVersionCode = BuildConfig.VERSION_CODE.toLong()

    // ====== Interface methods
    override suspend fun init() {
        // Read last version
        lastVersionCode =
            context.musekitPreferencesDataStore.data
                .map { preferences ->
                    preferences.lastVersionCode
                }.first()

        // Run updates
        Log.i(
            "Update manager",
            "Last version code: $lastVersionCode, current version code: $currentVersionCode",
        )

//        onUpdate(from = 1721768603) { migratePresets() }

        // Update last version
        updateLastVersionCode()
    }

    // ====== Private methods
    private suspend fun onUpdate(
        from: Long,
        run: suspend () -> Unit,
    ) {
        if (lastVersionCode <= from) {
            run()
        }
    }

    private suspend fun updateLastVersionCode() {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences
                .toBuilder()
                .setLastVersionCode(BuildConfig.VERSION_CODE.toLong())
                .build()
        }
    }

    private suspend fun migratePresets() {
        val oldPresetManager = PresetsManagerV1Impl(context)

        val oldPresets = oldPresetManager.getPresets()
        oldPresets.forEach { (name, semitones, octave, pitch) ->
            val newPreset =
                Preset(
                    id = 0,
                    name = name,
                    semitones = semitones,
                    octave = octave,
                    pitch = pitch,
                )

            presetsManager.savePreset(newPreset)
            oldPresetManager.removePreset(name)
        }
    }
}
