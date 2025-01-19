package com.kwasow.musekit.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.kwasow.musekit.R
import com.kwasow.musekit.adapters.SoundsAdapter
import com.kwasow.musekit.databinding.FragmentMetronomeBinding
import com.kwasow.musekit.services.MetronomeService

class MetronomeFragment : Fragment() {

    // ====== Fields
    private lateinit var buttonStartStop: MaterialButton
    private lateinit var buttonMinus5: MaterialButton
    private lateinit var buttonMinus2: MaterialButton
    private lateinit var buttonMinus1: MaterialButton
    private lateinit var buttonPlus1: MaterialButton
    private lateinit var buttonPlus2: MaterialButton
    private lateinit var buttonPlus5: MaterialButton

    private lateinit var textBpm: AppCompatTextView
    private lateinit var sliderBeat: Slider
    private lateinit var metronomeSoundPicker: AutoCompleteTextView

    private lateinit var metronomeService: MetronomeService
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MetronomeService.LocalBinder
            metronomeService = binder.getService()
            isBound = true
            metronomeService.connectTicker(sliderBeat)

            updateBpm()
            setupSoundPicker()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    // ====== Interface methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMetronomeBinding.inflate(inflater)

        buttonStartStop = binding.buttonStartStop
        buttonMinus5 = binding.buttonMinus5
        buttonMinus2 = binding.buttonMinus2
        buttonMinus1 = binding.buttonMinus1
        buttonPlus1 = binding.buttonPlus1
        buttonPlus2 = binding.buttonPlus2
        buttonPlus5 = binding.buttonPlus5

        textBpm = binding.textBpm
        sliderBeat = binding.sliderBeat
        metronomeSoundPicker = binding.metronomeSoundPicker

        // Prevent sleep while metronome is active
        binding.root.keepScreenOn = true

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bindService()
        setupButtons()
    }

    override fun onStop() {
        super.onStop()

        requireContext().unbindService(serviceConnection)
        isBound = false
    }

    // ====== Private methods
    private fun bindService() {
        Intent(requireContext(), MetronomeService::class.java).also { intent ->
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun setupButtons() {
        buttonStartStop.setOnClickListener {
            if (isBound) {
                if (metronomeService.isPlaying) {
                    buttonStartStop.apply {
                        setIconResource(R.drawable.anim_pause_to_play)
                        (icon as AnimatedVectorDrawable).start()
                    }
                } else {
                    buttonStartStop.apply {
                        setIconResource(R.drawable.anim_play_to_pause)
                        (icon as AnimatedVectorDrawable).start()
                    }
                }
                metronomeService.startStopMetronome()
            }
        }

        buttonPlus5.setOnClickListener { updateBpm(5) }
        buttonPlus2.setOnClickListener { updateBpm(2) }
        buttonPlus1.setOnClickListener { updateBpm(1) }

        buttonMinus5.setOnClickListener { updateBpm(-5) }
        buttonMinus2.setOnClickListener { updateBpm(-2) }
        buttonMinus1.setOnClickListener { updateBpm(-1) }
    }

    private fun updateBpm(by: Int = 0) {
        if (isBound) {
            metronomeService.bpm += by
            textBpm.text = String.format(metronomeService.bpm.toString())
        }
    }

    private fun setupSoundPicker() {
        if (isBound) {
            val sounds = metronomeService.getAvailableSounds()
            val soundNames = sounds.map { getString(it.resourceNameId) }
            val soundsAdapter = SoundsAdapter(requireContext(), soundNames)

            metronomeSoundPicker.setAdapter(soundsAdapter)
            metronomeSoundPicker.setText(
                getString(metronomeService.sound.resourceNameId),
                false
            )
            metronomeSoundPicker.setOnItemClickListener { _, _, i, _ ->
                if (isBound) metronomeService.sound = sounds[i]
            }
        }
    }
}
