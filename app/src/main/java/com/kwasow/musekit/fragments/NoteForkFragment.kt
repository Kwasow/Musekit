package com.kwasow.musekit.fragments

import android.media.AudioTrack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kwasow.musekit.Note
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.FragmentNoteForkBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.properties.Delegates

class NoteForkFragment : Fragment() {
    private lateinit var binding: FragmentNoteForkBinding
    private lateinit var note: Note

    private lateinit var player: AudioTrack
    private var playing = false
    private var sampleRate by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteForkBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        note = Note()

        refreshTextViews()
        setupPlayer()
        setupListeners()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::player.isInitialized) {
            player.release()
        }
    }

    private fun refreshTextViews() {
        binding.textPitch.text = getString(R.string.pitch_placeholder, note.pitch)
        binding.textNote.text = getString(R.string.note_placeholder, note.getNoteName(), note.octave)
    }

    private fun setupPlayer() {
        player = AudioTrack.Builder()
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
        sampleRate = player.sampleRate
    }

    private fun setupListeners() {
        binding.buttonNoteUp.setOnClickListener {
            note.up()
            refreshTextViews()
            restartPlayer()
        }

        binding.buttonNoteDown.setOnClickListener {
            val oldNoteFrequency = note.getFrequency()
            note.down()
            if (oldNoteFrequency != note.getFrequency()) {
                refreshTextViews()
                restartPlayer()
            }
        }

        binding.buttonPitchUp.setOnClickListener {
            note.pitch += 1
            refreshTextViews()
            restartPlayer()
        }

        binding.buttonPitchDown.setOnClickListener {
            note.pitch -= 1
            refreshTextViews()
            restartPlayer()
        }

        binding.buttonStartStop.setOnClickListener {
            if (!playing) {
                playing = true
                playSound()
            } else {
                playing = false
                stopSound()
            }
        }
    }

    private fun restartPlayer() {
        stopSound()
        playSound()
    }

    private fun playSound() {
        val tone = createSineWave(note.getFrequency())

        player.play()

        GlobalScope.launch {
            val notePlaying = note.getFrequency()
            while (playing && notePlaying == note.getFrequency()) {
                // Only write if the buffer is about to end to safe memory
                if (player.bufferSizeInFrames < sampleRate) {
                    player.write(tone, 0, tone.size)
                }
            }
        }
    }

    private fun createSineWave(frequency: Double): ShortArray {
        val numberOfSamples = 10 * sampleRate
        val samples = DoubleArray(numberOfSamples)
        val buffer = ShortArray(numberOfSamples)

        for (i in 0 until numberOfSamples) {
            samples[i] = sin(i * 2 * Math.PI * frequency / sampleRate)
            buffer[i] = (samples[i] * Short.MAX_VALUE).toInt().toShort()
        }

        return buffer
    }

    private fun stopSound() {
        player.stop()
        player.flush()
    }
}