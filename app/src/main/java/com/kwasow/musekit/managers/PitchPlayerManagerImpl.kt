package com.kwasow.musekit.managers

import android.media.AudioTrack
import kotlin.math.sin

class PitchPlayerManagerImpl : PitchPlayerManager {
    // ====== Fields
    companion object {
        private val SAMPLE_RATE = AudioTrack.getNativeOutputSampleRate(AudioTrack.MODE_STATIC)
    }

    override var frequency: Double = 440.0
        set(value) {
            field = value

            reloadTone()
        }

    private val audioTrack by lazy {
        AudioTrack.Builder()
            .setTransferMode(AudioTrack.MODE_STATIC)
            .setBufferSizeInBytes(SAMPLE_RATE * 2)
            .build()
    }

    // ====== Interface methods
    override fun play() {
        reloadTone()
        audioTrack.play()
    }

    override fun stop() {
        audioTrack.stop()
    }

    // ====== Private methods
    private fun reloadTone() {
        val tone = createSineWave(frequency)

        audioTrack.write(tone, 0, tone.size)
        val loopEnd = audioTrack.bufferSizeInFrames
        audioTrack.setLoopPoints(0, loopEnd, -1)
    }

    private fun createSineWave(frequency: Double): ShortArray {
        val buffer = ShortArray(SAMPLE_RATE * 2)

        for (i in buffer.indices) {
            val sample = sin(i * Math.PI * frequency.toInt() / SAMPLE_RATE)
            buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }

        return buffer
    }
}
