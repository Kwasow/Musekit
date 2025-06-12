package com.kwasow.musekit.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.MutableLiveData
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

    val isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
    val tickerPosition: MutableLiveData<Float> = MutableLiveData(0f)
    val currentBeat: MutableLiveData<Int> = MutableLiveData(1)

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
                .setMaxStreams(10)
                .build()

        soundId = soundPool.load(this, sound.resourceId, 1)
        handler = Handler(Looper.getMainLooper())

        setupCollectors()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancelChildren()

        if (this::soundPool.isInitialized) {
            soundPool.release()
        }
    }

    override fun run() {
        if (isPlaying.value == true) {
            if (sound != MetronomeSounds.None) {
                soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
            }
            currentBeat.value?.let { old ->
                currentBeat.postValue(old % 4 + 1)
            }

            handler.postDelayed(this, (1000L * 60) / bpm)
        }
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
    private fun startMetronome() {
        isPlaying.postValue(true)
        currentBeat.postValue(0)
        handler.post(this)
    }

    private fun stopMetronome() {
        isPlaying.postValue(false)
    }

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
            }
        }

        coroutineScope.launch {
            preferencesManager.metronomeNumberOfBeats.collect { collected ->
                numberOfBeats = collected
            }
        }
    }
}
