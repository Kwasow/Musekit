package com.kwasow.musekit.fragments

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.FragmentMetronomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MetronomeFragment : Fragment() {
    private lateinit var binding: FragmentMetronomeBinding
    private var bpm by Delegates.notNull<Int>()

    private lateinit var soundPool: SoundPool
    private var playing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMetronomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setupDefaultBpm()
        setupPlayer()
        setupListeners()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::soundPool.isInitialized)
            soundPool.release()
    }

    private fun setupDefaultBpm() {
        bpm = binding.sliderTempo.value.toInt()
    }

    private fun setupPlayer() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun setupListeners() {
        binding.sliderTempo.addOnChangeListener { _, value, _ ->
            bpm = value.toInt()
        }

        binding.buttonStartStop.setOnClickListener {
            if (!playing) {
                playing = true
                startMetronome()
            } else {
                playing = false
            }
        }
    }

    private fun startMetronome() {
        val sound = getSound()

        // TODO: Bug around 120-130 - it's not in time
        GlobalScope.launch {
            while (playing) {
                GlobalScope.launch {
                    soundPool.play(sound, 1.0f, 1.0f, 1, 0, 1f)
                }
                delay(1000L * 60 / bpm)
            }
        }
    }

    private fun getSound(): Int {
        // TODO: Different sounds
        return soundPool.load(context, R.raw.metronome_beep, 1)
    }
}