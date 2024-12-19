package com.kwasow.musekit.utils

import androidx.lifecycle.MutableLiveData
import be.tarsos.dsp.filters.HighPass
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.kwasow.musekit.data.Note

class MusekitPitchDetector {
    // ====== Fields
    companion object {
        private const val MINIMUM_FREQ = 55f
        private const val SAMPLING_RATE = 44100
        private const val BUFFER_SIZE = 1024 * 4
        private const val OVERLAP = 0

        private val ALGORITHM = PitchEstimationAlgorithm.FFT_YIN

        fun findNoteDetails(frequency: Double): Pair<Note, Double>? {
            if (frequency < MINIMUM_FREQ) {
                return null
            }

            val note = Note()
            val closeness: Double

            if (note.getFrequency() < frequency) {
                while (note.getFrequency() < frequency) {
                    note.up()
                }

                val previousNote = Note(note)
                previousNote.down()

                val middle = (previousNote.getFrequency() + note.getFrequency()) / 2.0
                if (frequency >= middle) {
                    closeness = -1 + ((frequency - middle) / (note.getFrequency() - middle))
                } else {
                    note.down()
                    closeness = (frequency - note.getFrequency()) / (middle - note.getFrequency())
                }
            } else {
                while (note.getFrequency() > frequency) {
                    note.down()
                }

                val previousNote = Note(note)
                previousNote.up()

                val middle = (previousNote.getFrequency() + note.getFrequency()) / 2.0
                if (frequency >= middle) {
                    note.up()
                    closeness = -1 + ((frequency - middle) / (note.getFrequency() - middle))
                } else {
                    closeness = (frequency - note.getFrequency()) / (middle - note.getFrequency())
                }
            }

            return Pair(note, closeness)
        }
    }

    private val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(
        SAMPLING_RATE,
        BUFFER_SIZE,
        OVERLAP
    )

    private val pitchDetectionHandler = PitchDetectionHandler { res, _ ->
        currentPitch.postValue(res.pitch.toDouble())
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
