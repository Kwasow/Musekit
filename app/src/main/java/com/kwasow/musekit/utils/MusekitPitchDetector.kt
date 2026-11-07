package com.kwasow.musekit.utils

import android.os.CountDownTimer
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.filters.HighPass
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.PitchDetectionResult
import com.kwasow.musekit.extensions.mostCommon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.log2

class MusekitPitchDetector(
    private val dispatcher: AudioDispatcher,
    private val getPitch: () -> Int,
) {
    // ====== Fields
    companion object {
        private const val MINIMUM_FREQ = 55f
        private const val MIN_ITEM_COUNT = 15
        private const val SAMPLING_RATE = 44100
        private const val BUFFER_SIZE = 1024 * 4
        private const val OVERLAP = 768 * 4

        private val ALGORITHM = PitchEstimationAlgorithm.FFT_YIN

        fun findNoteDetails(
            frequency: Double,
            pitch: Int = 440,
        ): Pair<Note, Double>? {
            if (frequency < MINIMUM_FREQ) {
                return null
            }

            val centDiff: Double = 1200.0 * log2(frequency / pitch)

            val note = Note.fromCentDiff(centDiff, pitch)
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
                OVERLAP,
            )
    }

    private val timer =
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) = Unit

            override fun onFinish() {
                _detectionResult.value = null
            }
        }

    private val history = mutableListOf<Pair<Note, Double>>()
    private val pitchDetectionHandler =
        PitchDetectionHandler { res, _ ->
            val pitch = res.pitch
            val basePitch = getPitch()

            val recognized = findNoteDetails(pitch.toDouble(), basePitch)
            if (recognized != null) {
                history.add(recognized)
            }

            if (history.size >= MIN_ITEM_COUNT) {
                timer.cancel()
                timer.start()

                _detectionResult.value = calculateAverage()
                history.clear()
            }
        }

    private val _detectionResult: MutableStateFlow<PitchDetectionResult?> = MutableStateFlow(null)
    val detectionResult = _detectionResult.asStateFlow()

    // ====== Constructors
    init {
        val highPassProcessor = HighPass(MINIMUM_FREQ, 0f)

        val pitchProcessor =
            PitchProcessor(
                ALGORITHM,
                SAMPLING_RATE.toFloat(),
                BUFFER_SIZE,
                this.pitchDetectionHandler,
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

    // ====== Private methods
    private fun calculateAverage(): PitchDetectionResult {
        val mostCommon = history.map { it.first }.mostCommon()
        val filtered = history.filter { it.first == mostCommon }

        val avgCents = filtered.map { it.second }.average()
        return PitchDetectionResult(note = mostCommon, cents = avgCents)
    }
}
