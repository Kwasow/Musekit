package com.kwasow.musekit.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.kwasow.musekit.R
import kotlin.properties.Delegates

class MetronomeService : Service(), Runnable {

    enum class Sounds(val resourceId: Int, val resourceName: String) {
        Default(R.raw.metronome_click, "Default"),
        Beep(R.raw.metronome_beep, "Beep"),
        Ding(R.raw.metronome_ding, "Ding"),
        Wood(R.raw.metronome_wood, "Wood")
    }
    private var soundBeep by Delegates.notNull<Int>()

    inner class LocalBinder : Binder() {
        fun getService(): MetronomeService = this@MetronomeService
    }

    private var binder = LocalBinder()

    private val soundsList = listOf(Sounds.Default, Sounds.Beep, Sounds.Ding, Sounds.Wood)
    var sound = Sounds.Default
    var bpm = 60

    private var isPlaying = false
    private lateinit var soundPool: SoundPool
    private lateinit var handler: Handler

    override fun onCreate() {
        super.onCreate()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(3)
            .build()

        soundBeep = soundPool.load(this, R.raw.metronome_beep, 1)

        handler = Handler(Looper.getMainLooper())
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::soundPool.isInitialized) {
            soundPool.release()
        }
    }

    fun startStopMetronome() {
        if (isPlaying) {
            stopMetronome()
        } else {
            startMetronome()
        }
    }

    private fun startMetronome() {
        isPlaying = true
        handler.post(this)
    }

    private fun stopMetronome() {
        isPlaying = false
    }

    override fun run() {
        soundPool.play(sound.resourceId, 1F, 1F, 0, 0, 1F)

        if (isPlaying) {
            handler.postDelayed(this, (1000L * 60) / bpm)
        }
    }

    fun getAvailableSounds() = soundsList
}