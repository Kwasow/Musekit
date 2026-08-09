package com.kwasow.musekit.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Binder
import kotlin.math.min
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.WorklogManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import kotlin.properties.Delegates

class MetronomeService :
    Service() {
    // ====== Fields
    inner class LocalBinder : Binder() {
        val service: MetronomeService = this@MetronomeService
    }

    private var binder = LocalBinder()

    private val preferencesManager by inject<PreferencesManager>()
    private val worklogManager by inject<WorklogManager>()

    private var soundId by Delegates.notNull<Int>()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private var sound = MetronomeSounds.Default
    private var audioData: MutableLiveData<ByteArray> = MutableLiveData(byteArrayOf())
    private var interval = toInterval(60)
    private var numberOfBeats = 4

    private var sessionStart: LocalDateTime? = null

    val isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
    val currentBeat: MutableLiveData<Int> = MutableLiveData(1)

    // ====== Interface methods
    override fun onCreate() {
        super.onCreate()
        soundId = sound.getResourceId()
        setupCollectors()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onUnbind(intent: Intent?): Boolean = super.onUnbind(intent)

    override fun onDestroy() {
        super.onDestroy()
        job.cancelChildren()
        stopMetronome()
    }

    // ====== Public methods
    fun startStopMetronome() {
        if (isPlaying.value == true) {
            stopMetronome()
        } else {
            startMetronome()
        }
    }

    // ====== Private methods
    private fun toInterval(bpm: Int): Long = (1000L * 60) / bpm

    private fun startMetronome() {
        currentBeat.value = 0
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormatEncoding = AudioFormat.ENCODING_PCM_16BIT
        val sampleRate = getPreferredSampleRate()

        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            channelConfig,
            audioFormatEncoding
        )
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setChannelMask(channelConfig)
                    .setEncoding(audioFormatEncoding)
                    .setSampleRate(sampleRate)
                    .build()
            )
            .setBufferSizeInBytes(minBufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM) // <-- Changed to STREAM mode
            .build()

        audioTrack.play()
        isPlaying.value = true

        // Generate audio on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            val buffer = ByteArray(minBufferSize)
            var currentSample = 0

            while (isPlaying.value == true) {
                // ms between beats = interval
                // samples between beats = sampleRate / (interval / 1000) * 2 (16bit sample)
                val samplesPerBeat = (sampleRate * (interval.toFloat() / 1000.0)).toInt() * 2

                for (i in buffer.indices) {
                    // First sample of the beat
                    if (currentSample == 0) {
                        currentBeat.value?.let { old ->
                            currentBeat.postValue(old % numberOfBeats + 1)
                        }
                    }

                    if (currentSample >= (audioData.value?.size ?: 0)) {
                        // beat audio over, fill with 0
                        buffer[i] = 0
                        currentSample += 1
                    } else {
                        // write audio into buffer
                        buffer[i] = audioData.getValue()?.get(currentSample) ?: 0
                        currentSample += 1
                    }
                    currentSample %= samplesPerBeat
                }

                val result = audioTrack.write(buffer, 0, buffer.size)
                if (result < 0) break
            }
        }
        sessionStart = LocalDateTime.now()
    }

    private fun stopMetronome() {
        sessionStart?.let { start ->
            coroutineScope.launch {
                worklogManager.addWorklogEntry(start, LocalDateTime.now())
            }
        }
        sessionStart = null
        isPlaying.value = false
    }

    fun getPreferredSampleRate(): Int {
        return AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC)
    }

    // Load wav file into audioData
    private fun loadAudioData() {
        if (soundId != MetronomeSounds.None.getResourceId()) {
            val inputStream = resources.openRawResource(soundId)
            // skip header
            inputStream.skip(44)
            audioData.postValue(inputStream.readBytes())
            inputStream.close()
        } else {
            audioData.postValue(ByteArray(0))
        }
    }

    private fun setupCollectors() {
        coroutineScope.launch {
            preferencesManager.metronomeSound.collect { collected ->
                soundId = collected.getResourceId()
                loadAudioData()
            }
        }

        coroutineScope.launch {
            preferencesManager.metronomeBpm.collect { collected ->
                interval = toInterval(collected)
            }
        }

        coroutineScope.launch {
            preferencesManager.metronomeNumberOfBeats.collect { collected ->
                numberOfBeats = collected
            }
        }
    }
}
