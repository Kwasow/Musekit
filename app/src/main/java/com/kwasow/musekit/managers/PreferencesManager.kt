package com.kwasow.musekit.managers

import com.kwasow.musekit.data.NotationStyle
import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    // ====== Fields
    val nightMode: Flow<Int>

    val noteForkMode: Flow<Int>

    val automaticTunerPitch: Flow<Int>

    val metronomeBpm: Flow<Int>

    val notationStyle: Flow<NotationStyle>

    // ====== Methods
    suspend fun setNightMode(value: Int)

    suspend fun setNoteForkMode(value: Int)

    suspend fun setAutomaticTunerPitch(value: Int)

    suspend fun setMetronomeBPM(value: Int)

    suspend fun setNotationStyle(value: NotationStyle)
}
