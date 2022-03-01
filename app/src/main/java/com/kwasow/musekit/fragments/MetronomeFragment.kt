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
import androidx.fragment.app.Fragment
import com.kwasow.musekit.R
import com.kwasow.musekit.adapters.SoundsAdapter
import com.kwasow.musekit.databinding.FragmentMetronomeBinding
import com.kwasow.musekit.services.MetronomeService

class MetronomeFragment : Fragment() {
    private lateinit var binding: FragmentMetronomeBinding

    private lateinit var metronomeService: MetronomeService
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MetronomeService.LocalBinder
            metronomeService = binder.getService()
            isBound = true

            setupBpmText()
            setupSoundPicker()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

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

        bindService()
        setupButtons()
    }

    override fun onStop() {
        super.onStop()

        requireContext().unbindService(serviceConnection)
        isBound = false
    }

    private fun bindService() {
        Intent(requireContext(), MetronomeService::class.java).also {intent ->
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun setupButtons() {
        binding.buttonStartStop.setOnClickListener {
            if (isBound) {
                if (metronomeService.isPlaying) {
                    binding.buttonStartStop.apply {
                        setIconResource(R.drawable.anim_pause_to_play)
                        (icon as AnimatedVectorDrawable).start()
                    }
                } else {
                    binding.buttonStartStop.apply {
                        setIconResource(R.drawable.anim_play_to_pause)
                        (icon as AnimatedVectorDrawable).start()
                    }
                }
                metronomeService.startStopMetronome()
            }
        }

        binding.buttonPlus5.setOnClickListener {
            if (isBound) {
                metronomeService.bpm += 5
                setupBpmText()
            }
        }

        binding.buttonPlus2.setOnClickListener {
            if (isBound) {
                metronomeService.bpm += 2
                setupBpmText()
            }
        }

        binding.buttonPlus1.setOnClickListener {
            if (isBound) {
                metronomeService.bpm += 1
                setupBpmText()
            }
        }

        binding.buttonMinus5.setOnClickListener {
            if (isBound) {
                metronomeService.bpm -= 5
                setupBpmText()
            }
        }

        binding.buttonMinus2.setOnClickListener {
            if (isBound) {
                metronomeService.bpm -= 2
                setupBpmText()
            }
        }

        binding.buttonMinus1.setOnClickListener {
            if (isBound) {
                metronomeService.bpm -= 1
                setupBpmText()
            }
        }
    }

    private fun setupBpmText() {
        if (isBound) binding.textBpm.text = metronomeService.bpm.toString()
    }

    private fun setupSoundPicker() {
        if (isBound) {
            val sounds = metronomeService.getAvailableSounds()
            val soundNames = sounds.map { it.resourceName }
            val soundsAdapter = SoundsAdapter(requireContext(), soundNames)

            binding.metronomeSoundPicker.setAdapter(soundsAdapter)
            binding.metronomeSoundPicker.setText(metronomeService.sound.resourceName, false)
            binding.metronomeSoundPicker.setOnItemClickListener { _, _, i, _ ->
                if (isBound) metronomeService.sound = sounds[i]
            }
        }
    }
}