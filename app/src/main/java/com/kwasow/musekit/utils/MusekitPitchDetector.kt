package com.kwasow.musekit.utils

import androidx.lifecycle.MutableLiveData
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.filters.HighPass
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Notes
import kotlin.math.abs
import kotlin.math.log2

class MusekitPitchDetector(
    private val dispatcher: AudioDispatcher
) {
    // ====== Fields
    companion object {
        private const val MINIMUM_FREQ = 55f
        private const val MIN_ITEM_COUNT = 15
        private const val SAMPLING_RATE = 44100
        private const val BUFFER_SIZE = 1024 * 4
        private const val OVERLAP = 768 * 4

        private val ALGORITHM = PitchEstimationAlgorithm.FFT_YIN

        fun findNoteDetails(frequency: Double): Pair<Note, Double>? {
            if (frequency < MINIMUM_FREQ) {
                return null
            }

            val baseFrequency = Note().getFrequency()
            val centDiff: Double = 1200.0 * log2(frequency / baseFrequency)

            val note = Note.fromCentDiff(centDiff)
            var cents = centDiff % 100
            if (cents > 50) {
                cents -= 100
            } else if (cents < -50) {
                cents += 100
            }

            return Pair(note, cents)
        }

        fun buildDefaultDispatcher(): AudioDispatcher =
            AudioDispatcherFactory.fromDefaultMicrophone(
                SAMPLING_RATE,
                BUFFER_SIZE,
                OVERLAP
            )
    }

    private val history = mutableListOf<Double>()
    private val pitchDetectionHandler = PitchDetectionHandler { res, _ ->
        val pitch = res.pitch

        if (pitch == -1f) {
            return@PitchDetectionHandler
        }

        history.add(res.pitch.toDouble())

        if (history.size >= MIN_ITEM_COUNT) {
            currentPitch.postValue(history.average())
            history.clear()
        }
    }

    val currentPitch: MutableLiveData<Double> by lazy {
        MutableLiveData()
    }

    // ====== Constructors
    init {
        val highPassProcessor = HighPass(MINIMUM_FREQ, 0f)

        val pitchProcessor = PitchProcessor(
            ALGORITHM,
            SAMPLING_RATE.toFloat(),
            BUFFER_SIZE,
            this.pitchDetectionHandler
        )

        dispatcher.addAudioProcessor(highPassProcessor)
        dispatcher.addAudioProcessor(pitchProcessor)
    }

    // ====== Public methods
    fun startListening(): Boolean {
        Thread(dispatcher, "Audio dispatcher").start()

        return true
    }

    fun stopListening() {
        dispatcher.stop()
    }

}
