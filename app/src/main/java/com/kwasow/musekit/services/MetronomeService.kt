package com.kwasow.musekit.services

import android.animation.ValueAnimator
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

    private var right = true

    val isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
    val sound: MutableLiveData<MetronomeSounds> = MutableLiveData(MetronomeSounds.Default)
    val bpm: MutableLiveData<Int> = MutableLiveData(preferencesManager.getMetronomeBPM())
    val tickerPosition: MutableLiveData<Float> = MutableLiveData(0f)

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

        soundId = soundPool.load(this, sound.value!!.resourceId, 1)
        handler = Handler(Looper.getMainLooper())
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::soundPool.isInitialized) {
            soundPool.release()
        }
    }

    override fun run() {
        if (isPlaying.value == true) {
            val tickerAnimation = buildAnimation(bpm.value!!)
            if (sound.value != MetronomeSounds.None) {
                soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
            }
            tickerAnimation.start()

            handler.postDelayed(this, (1000L * 60) / bpm.value!!)
        }
    }

    // ====== Public methods
    fun setTempo(value: Int) {
        bpm.postValue(value)
    }

    fun updateTempo(by: Int) {
        bpm.postValue(bpm.value!! + by)
    }

    fun setSound(newSound: MetronomeSounds) {
        sound.postValue(newSound)

        if (newSound.resourceId != -1) {
            soundId = soundPool.load(this, newSound.resourceId, 1)
        }
    }

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
        handler.post(this)
    }

    private fun stopMetronome() {
        isPlaying.postValue(false)
    }

    private fun buildAnimation(bpm: Int): ValueAnimator {
        val tickerAnimation =
            if (right) {
                ValueAnimator.ofFloat(0F, 1F)
            } else {
                ValueAnimator.ofFloat(1F, 0F)
            }

        tickerAnimation.addUpdateListener {
            tickerPosition.postValue(it.animatedValue as Float)
        }

        tickerAnimation.duration = 1000L * 60 / bpm
        right = !right

        return tickerAnimation
    }
}
