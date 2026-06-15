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

    val metronomeNumberOfBeats: Flow<Int>

    val lastVersionCode: Flow<Long>

    val lastUsedTimestamp: Flow<Long>

    val daysUsedCounter: Flow<Long>

    // ====== Methods
    suspend fun setNightMode(value: Int)

    suspend fun setNoteForkMode(value: Int)

    suspend fun setNotationStyle(value: NotationStyle)

    suspend fun setAutomaticTunerPitch(value: Int)

    suspend fun setMetronomeBpm(value: Int)

    suspend fun setMetronomeSound(value: MetronomeSounds)

    suspend fun setMetronomeNumberOfBeats(value: Int)

    suspend fun setLastVersionCode(value: Long)

    suspend fun setLastUsedTimestamp(value: Long)

    suspend fun setDaysUsedCounter(value: Long)
}
