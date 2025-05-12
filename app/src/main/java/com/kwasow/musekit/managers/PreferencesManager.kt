package com.kwasow.musekit.managers

import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.data.NotationStyle
import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    // ====== Fields
    val nightMode: Flow<Int>

    val noteForkMode: Flow<Int>

    val notationStyle: Flow<NotationStyle>

    val automaticTunerPitch: Flow<Int>

    val metronomeBpm: Flow<Int>

    val metronomeSound: Flow<MetronomeSounds>

    // ====== Methods
    suspend fun setNightMode(value: Int)

    suspend fun setNoteForkMode(value: Int)

    suspend fun setNotationStyle(value: NotationStyle)

    suspend fun setAutomaticTunerPitch(value: Int)

    suspend fun setMetronomeBpm(value: Int)

    suspend fun setMetronomeSound(value: MetronomeSounds)
}
