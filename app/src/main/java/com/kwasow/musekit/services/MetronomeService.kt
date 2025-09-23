package com.kwasow.musekit.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.managers.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.properties.Delegates

class MetronomeService : Service(), Runnable {
    // ====== Fields
    inner class LocalBinder : Binder() {
        val service: MetronomeService = this@MetronomeService
    }

    private var binder = LocalBinder()

    private val preferencesManager by inject<PreferencesManager>()

    private var soundId by Delegates.notNull<Int>()
    private lateinit var soundPool: SoundPool
    private lateinit var handler: Handler

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private var sound = MetronomeSounds.Default
    private var bpm = 60
    private var numberOfBeats = 4

    var isPlaying = false
        private set
    private var currentIndex = 0
    private var interval = 0L

    var listener: TickListener? = null

    // ====== Inner classes
    interface TickListener {
        fun onStart()

        fun onTick(index: Int)

        fun onStop()
    }

    // ====== Interface methods
    override fun onCreate() {
        super.onCreate()

        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_UNKNOWN)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

        soundPool =
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build()

        soundId = soundPool.load(this, sound.resourceId, 1)
        handler = Handler(Looper.getMainLooper())

        setupCollectors()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancelChildren()

        if (this::soundPool.isInitialized) {
            soundPool.release()
        }
    }

    override fun run() {
        if (isPlaying) {
            handler.postDelayed(this, interval)

            if (sound != MetronomeSounds.None) {
                soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
            }

            if (currentIndex >= numberOfBeats) {
                currentIndex = 0
            }
            listener?.onTick(currentIndex++)
        }
    }

    // ====== Public methods
    fun startStopMetronome() {
        if (isPlaying) {
            listener?.onStop()
        } else {
            listener?.onStart()
            handler.post(this)
        }

        isPlaying = !isPlaying
    }

    // ====== Private methods
    private fun toInterval(bpm: Int): Long = (1000L * 60) / bpm

    private fun setupCollectors() {
        coroutineScope.launch {
            preferencesManager.metronomeSound.collect { collected ->
                sound = collected
                if (collected != MetronomeSounds.None) {
                    soundId = soundPool.load(this@MetronomeService, collected.resourceId, 1)
                }
            }
        }

        coroutineScope.launch {
            preferencesManager.metronomeBpm.collect { collected ->
                bpm = collected
                interval = toInterval(bpm)
            }
        }

        coroutineScope.launch {
            preferencesManager.metronomeNumberOfBeats.collect { collected ->
                numberOfBeats = collected
            }
        }
    }
}
