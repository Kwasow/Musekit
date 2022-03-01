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

    inner class LocalBinder : Binder() {
        fun getService(): MetronomeService = this@MetronomeService
    }

    private var binder = LocalBinder()

    private val soundsList = listOf(Sounds.Default, Sounds.Beep, Sounds.Ding, Sounds.Wood)
    var sound = Sounds.Default
    var bpm = 60

    private var soundDefault by Delegates.notNull<Int>()
    private var soundBeep by Delegates.notNull<Int>()
    private var soundDing by Delegates.notNull<Int>()
    private var soundWood by Delegates.notNull<Int>()

    var isPlaying = false
        private set
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

        soundDefault = soundPool.load(this, Sounds.Default.resourceId, 1)
        soundBeep = soundPool.load(this, Sounds.Beep.resourceId, 1)
        soundDing = soundPool.load(this, Sounds.Ding.resourceId, 1)
        soundWood = soundPool.load(this, Sounds.Wood.resourceId, 1)

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
        when (sound) {
            Sounds.Default -> soundPool.play(soundDefault, 1F, 1F, 0, 0, 1F)
            Sounds.Beep -> soundPool.play(soundBeep, 1F, 1F, 0, 0, 1F)
            Sounds.Ding -> soundPool.play(soundDing, 1F, 1F, 0, 0, 1F)
            Sounds.Wood -> soundPool.play(soundWood, 1F, 1F, 0, 0, 1F)
        }

        if (isPlaying) {
            handler.postDelayed(this, (1000L * 60) / bpm)
        }
    }

    fun getAvailableSounds() = soundsList
}