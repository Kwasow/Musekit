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

            if (audioTrack.state == AudioTrack.PLAYSTATE_PLAYING) {
                stop()
                play()
            }
        }

    private var audioTrack = AudioTrack.Builder()
        .setTransferMode(AudioTrack.MODE_STATIC)
        .setBufferSizeInBytes(SAMPLE_RATE * 2)
        .build()

    // ====== Interface methods
    override fun play() {
        val buffer = createSineWave(frequency)

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.setLoopPoints(0, buffer.size, -1)
        audioTrack.play()
    }

    override fun stop() {
        audioTrack.stop()
    }

    // ====== Private methods
    fun createSineWave(frequency: Double): ShortArray {
        val samples = DoubleArray(SAMPLE_RATE * 2)
        val buffer = ShortArray(SAMPLE_RATE * 2)

        for (i in buffer.indices) {
            samples[i] = sin(i * 2 * Math.PI * frequency.toInt() / SAMPLE_RATE)
            buffer[i] = (samples[i] * Short.MAX_VALUE).toInt().toShort()
        }

        return buffer
    }
}