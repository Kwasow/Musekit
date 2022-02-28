package com.kwasow.musekit.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kwasow.musekit.databinding.FragmentMetronomeBinding
import com.kwasow.musekit.services.MetronomeService
import kotlin.math.floor

class MetronomeFragment : Fragment() {
    private lateinit var binding: FragmentMetronomeBinding

    private lateinit var metronomeService: MetronomeService
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MetronomeService.LocalBinder
            metronomeService = binder.getService()
            isBound = true

            setupSlider()
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

        println("onStart metronome fragment")
        bindService()
        setupListeners()
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

    private fun setupListeners() {
        binding.buttonStartStop.setOnClickListener {
            if (isBound) {
                metronomeService.startStopMetronome()
            }
        }

        // TODO: Bug with last value not reported
        binding.sliderTempo.addOnChangeListener { _, value, _ ->
            if (isBound) {
                metronomeService.bpm = value.toInt()
            }
        }
    }

    private fun setupSlider() {
        if (isBound) {
            binding.sliderTempo.value = metronomeService.bpm.toFloat()
        }
    }
}