package com.kwasow.musekit.managers

import com.kwasow.musekit.data.NotationStyle

interface PreferencesManager {
    // ====== Fields
    fun getNightMode(default: Int): Int

    fun setNightMode(value: Int)

    fun getNoteForkMode(): Int

    fun setNoteForkMode(value: Int)

    fun getAutomaticTunerPitch(): Int

    fun setAutomaticTunerPitch(value: Int)

    fun getMetronomeBPM(): Int

    fun setMetronomeBPM(value: Int)

    fun getNotationStyle(): NotationStyle

    fun setNotationStyle(value: NotationStyle)
}
