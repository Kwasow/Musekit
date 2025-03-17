package com.kwasow.musekit.utils

import kotlin.math.roundToInt

class MusekitBeatDetector {
    // ====== Fields
    companion object {
        const val MS_IN_MINUTE = 60000.0
    }

    private var previous: Long? = null

    // ====== Public methods
    fun beatEvent(): Int? {
        val currentTime = System.currentTimeMillis()

        val res =
            previous?.let {
                val diff = currentTime - it
                return@let (MS_IN_MINUTE / diff).roundToInt()
            }

        previous = currentTime

        return res
    }
}
