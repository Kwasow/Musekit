package com.kwasow.musekit.managers

import android.content.Context
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.store.musekitPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManagerImpl(val context: Context) : PreferencesManager {
    // ====== Fields
    override val nightMode: Flow<Int> =
        context.musekitPreferencesDataStore.data
            .map { preferences ->
                preferences.nightMode
            }

    override val noteForkMode: Flow<Int> =
        context.musekitPreferencesDataStore.data
            .map { preferences ->
                preferences.noteForkMode
            }

    override val notationStyle: Flow<NotationStyle> =
        context.musekitPreferencesDataStore.data
            .map { preferences ->
                NotationStyle.valueOf(id = preferences.notationStyle) ?: NotationStyle.English
            }

    override val automaticTunerPitch: Flow<Int> =
        context.musekitPreferencesDataStore.data
            .map { preferences ->
                preferences.automaticTunerPitch
            }

    override val metronomeBpm: Flow<Int> =
        context.musekitPreferencesDataStore.data
            .map { preferences ->
                preferences.metronomeBpm
            }

    override val metronomeSound: Flow<MetronomeSounds> =
        context.musekitPreferencesDataStore.data
            .map { preferences ->
                MetronomeSounds.valueOf(preferences.metronomeSound) ?: MetronomeSounds.Default
            }

    // ====== Public methods
    override suspend fun setNightMode(value: Int) {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setNightMode(value)
                .build()
        }
    }

    override suspend fun setNoteForkMode(value: Int) {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setNoteForkMode(value)
                .build()
        }
    }

    override suspend fun setNotationStyle(value: NotationStyle) {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setNotationStyle(value.id)
                .build()
        }
    }

    override suspend fun setAutomaticTunerPitch(value: Int) {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setAutomaticTunerPitch(value)
                .build()
        }
    }

    override suspend fun setMetronomeBpm(value: Int) {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setMetronomeBpm(value.coerceIn(30, 300))
                .build()
        }
    }

    override suspend fun setMetronomeSound(value: MetronomeSounds) {
        context.musekitPreferencesDataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setMetronomeSound(value.id)
                .build()
        }
    }
}
