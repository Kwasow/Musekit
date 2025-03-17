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
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.google.android.material.slider.Slider
import com.kwasow.musekit.R
import com.kwasow.musekit.utils.MusekitPreferences
import kotlin.properties.Delegates

class MetronomeService : Service(), Runnable {
    // ====== Fields
    enum class Sounds(
        @RawRes val resourceId: Int,
        @StringRes val resourceNameId: Int,
    ) {
        Default(R.raw.metronome_click, R.string.metronome_sound_default),
        Beep(R.raw.metronome_beep, R.string.metronome_sound_beep),
        Ding(R.raw.metronome_ding, R.string.metronome_sound_ding),
        Wood(R.raw.metronome_wood, R.string.metronome_sound_wood),
        None(-1, R.string.metronome_sound_none),
    }

    inner class LocalBinder : Binder() {
        fun getService(): MetronomeService = this@MetronomeService
    }

    private var binder = LocalBinder()

    private val soundsList = Sounds.entries.toTypedArray()
    var sound = Sounds.Default
        set(value) {
            field = value

            if (value.resourceId != -1) {
                soundId = soundPool.load(this, value.resourceId, 1)
            }
        }
    var bpm = MusekitPreferences.metronomeBPM
        set(value) {
            field = value.coerceIn(30..300)
            MusekitPreferences.metronomeBPM = field
        }

    private var soundId by Delegates.notNull<Int>()

    var isPlaying = false
        private set
    private lateinit var soundPool: SoundPool
    private lateinit var handler: Handler

    private var ticker: Slider? = null
    private var right = true

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

        soundId = soundPool.load(this, Sounds.Default.resourceId, 1)
        handler = Handler(Looper.getMainLooper())
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        ticker = null

        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::soundPool.isInitialized) {
            soundPool.release()
        }
    }

    override fun run() {
        if (isPlaying) {
            val tickerAnimation = buildAnimation(bpm)
            if (sound != Sounds.None) {
                soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
            }
            tickerAnimation.start()

            handler.postDelayed(this, (1000L * 60) / bpm)
        }
    }

    // ====== Public methods
    fun connectTicker(slider: Slider) {
        ticker = slider
    }

    fun startStopMetronome() {
        if (isPlaying) {
            stopMetronome()
        } else {
            startMetronome()
        }
    }

    fun getAvailableSounds() = soundsList

    // ====== Private methods
    private fun startMetronome() {
        isPlaying = true
        ticker?.isEnabled = true
        handler.post(this)
    }

    private fun stopMetronome() {
        ticker?.isEnabled = false
        isPlaying = false
    }

    private fun buildAnimation(bpm: Int): ValueAnimator {
        val tickerAnimation =
            if (right) {
                ValueAnimator.ofFloat(0F, 1F)
            } else {
                ValueAnimator.ofFloat(1F, 0F)
            }

        tickerAnimation.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            ticker?.value = animatedValue
        }

        tickerAnimation.duration = 1000L * 60 / bpm
        right = !right

        return tickerAnimation
    }
}
