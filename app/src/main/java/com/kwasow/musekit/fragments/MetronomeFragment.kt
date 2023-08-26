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
import kotlin.math.max

class MetronomeFragment : Fragment() {
    private lateinit var binding: FragmentMetronomeBinding

    private lateinit var metronomeService: MetronomeService
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MetronomeService.LocalBinder
            metronomeService = binder.getService()
            isBound = true
            metronomeService.connectTicker(binding.sliderBeat)

            updateBpm()
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
        Intent(requireContext(), MetronomeService::class.java).also { intent ->
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

        binding.buttonPlus5.setOnClickListener { updateBpm(5) }
        binding.buttonPlus2.setOnClickListener { updateBpm(2) }
        binding.buttonPlus1.setOnClickListener { updateBpm(1) }

        binding.buttonMinus5.setOnClickListener { updateBpm(-5) }
        binding.buttonMinus2.setOnClickListener { updateBpm(-2) }
        binding.buttonMinus1.setOnClickListener { updateBpm(-1) }
    }

    private fun updateBpm(by: Int = 0) {
        if (isBound) {
            val newBpm = max(1, metronomeService.bpm + by)

            metronomeService.bpm += newBpm
            binding.textBpm.text = metronomeService.bpm.toString()
        }
    }

    private fun setupSoundPicker() {
        if (isBound) {
            val sounds = metronomeService.getAvailableSounds()
            val soundNames = sounds.map { getString(it.resourceNameId) }
            val soundsAdapter = SoundsAdapter(requireContext(), soundNames)

            binding.metronomeSoundPicker.setAdapter(soundsAdapter)
            binding.metronomeSoundPicker.setText(
                getString(metronomeService.sound.resourceNameId),
                false
            )
            binding.metronomeSoundPicker.setOnItemClickListener { _, _, i, _ ->
                if (isBound) metronomeService.sound = sounds[i]
            }
        }
    }
}
