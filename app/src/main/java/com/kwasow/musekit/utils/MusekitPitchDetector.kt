package com.kwasow.musekit.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder.AudioSource
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.android.AndroidAudioInputStream
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm

class MusekitPitchDetector {
    // ====== Fields
    companion object {
        private const val SAMPLING_RATE = 44100
        private const val BUFFER_SIZE = 1024
        private const val OVERLAP = 0

        private val ALGORITHM = PitchEstimationAlgorithm.FFT_YIN
    }

    private var dispatcher: AudioDispatcher? = null
    private var audioInputStream: AudioRecord? = null

    // ====== Public methods
    fun startListening(pitchDetectionHandler: PitchDetectionHandler): Boolean {
        if (!prepare()) {
            return false
        }

        val processor = PitchProcessor(
            ALGORITHM,
            SAMPLING_RATE.toFloat(),
            BUFFER_SIZE,
            pitchDetectionHandler
        )

        dispatcher?.addAudioProcessor(processor)

        audioInputStream?.startRecording()

        if (audioInputStream?.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
            return false
        }

        Thread(dispatcher, "Audio dispatcher").start()
        return true
    }

    fun stopListening() {
        dispatcher?.stop()

        try {
            audioInputStream?.stop()
        } catch (_: Exception) {
            // Sometimes AudioRecord.stop() throws an exception. I don't know why,
            // but it seems safe to ignore it here since we're done with audioInputStream
            // anyway.
        }

        audioInputStream?.release()
        audioInputStream = null
        dispatcher = null
    }

    // ====== Private methods
    private fun prepare(): Boolean {
        val minBufferSize = AudioRecord.getMinBufferSize(
            SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (minBufferSize < 0) {
            return false
        }

        try {
            audioInputStream = AudioRecord(
                AudioSource.MIC,
                SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE
            )
        } catch (e: SecurityException) {
            // Microphone permission was not granted
            return false
        }

        if (audioInputStream?.state != AudioRecord.STATE_INITIALIZED) {
            return false
        }

        val audioFormat = TarsosDSPAudioFormat(
            SAMPLING_RATE.toFloat(),
            16,
            1,
            true,
            false
        )
        val audioStream = AndroidAudioInputStream(audioInputStream, audioFormat)
        dispatcher = AudioDispatcher(audioStream, BUFFER_SIZE, OVERLAP)

        return true
    }
}
