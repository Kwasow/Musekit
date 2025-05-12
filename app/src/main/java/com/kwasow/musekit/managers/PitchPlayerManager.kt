package com.kwasow.musekit.managers

interface PitchPlayerManager {
    // ====== Fields
    var frequency: Double

    // ====== Methods
    fun play()

    fun stop()
}
