package com.kwasow.musekit.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
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
    Service(),
    Runnable {
    // ====== Fields
    inner class LocalBinder : Binder() {
        val service: MetronomeService = this@MetronomeService
    }

    private var binder = LocalBinder()

    private val preferencesManager by inject<PreferencesManager>()
    private val worklogManager by inject<WorklogManager>()

    private var soundId by Delegates.notNull<Int>()
    private lateinit var soundPool: SoundPool

    private lateinit var handlerThread: HandlerThread
    private lateinit var beatHandler: Handler

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private var sound = MetronomeSounds.Default
    private var interval = toInterval(60)
    private var numberOfBeats = 4

    private var sessionStart: LocalDateTime? = null

    val isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
    val currentBeat: MutableLiveData<Int> = MutableLiveData(1)

    // ====== Interface methods
    override fun onCreate() {
        super.onCreate()

        val audioAttributes =
            AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_UNKNOWN)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

        soundPool =
            SoundPool
                .Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build()

        soundId = soundPool.load(this, sound.getResourceId(), 1)

        handlerThread = HandlerThread("MetronomeServiceHandlerThread")
        handlerThread.start()

        beatHandler = Handler(handlerThread.looper)

        setupCollectors()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onUnbind(intent: Intent?): Boolean = super.onUnbind(intent)

    override fun onDestroy() {
        super.onDestroy()
        job.cancelChildren()

        // Stop metronome callbacks
        if (this::beatHandler.isInitialized) {
            beatHandler.removeCallbacks(this)
        }

        // Release audio resources
        if (this::soundPool.isInitialized) {
            soundPool.release()
        }

        // Shut down background looper thread
        if (this::handlerThread.isInitialized) {
            handlerThread.quitSafely()
        }
    }

    override fun run() {
        if (isPlaying.value == true) {
            beatHandler.postDelayed(this, interval)

            if (sound != MetronomeSounds.None) {
                soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
            }
            currentBeat.value?.let { old ->
                currentBeat.postValue(old % numberOfBeats + 1)
            }
        }
    }

    // ====== Public methods
    fun startStopMetronome() {
        if (isPlaying.value == true) {
            stopMetronome()

            sessionStart?.let { start ->
                coroutineScope.launch {
                    worklogManager.addWorklogEntry(start, LocalDateTime.now())
                }
            }
            sessionStart = null
        } else {
            sessionStart = LocalDateTime.now()
            startMetronome()
        }
    }

    // ====== Private methods
    private fun toInterval(bpm: Int): Long = (1000L * 60) / bpm

    private fun startMetronome() {
        isPlaying.value = true
        currentBeat.value = 0
        beatHandler.post(this)
    }

    private fun stopMetronome() {
        isPlaying.value = false

        if (this::beatHandler.isInitialized) {
            beatHandler.removeCallbacks(this)
        }
    }

    private fun setupCollectors() {
        coroutineScope.launch {
            preferencesManager.metronomeSound.collect { collected ->
                sound = collected
                if (collected != MetronomeSounds.None) {
                    soundId = soundPool.load(this@MetronomeService, collected.getResourceId(), 1)
                }
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
