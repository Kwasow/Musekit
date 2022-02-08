package com.kwasow.musekit.fragments

import android.content.DialogInterface
import android.media.AudioTrack
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.R
import com.kwasow.musekit.adapters.PresetsAdapter
import com.kwasow.musekit.data.Notes
import com.kwasow.musekit.data.PresetsManager
import com.kwasow.musekit.databinding.DialogSavePresetBinding
import com.kwasow.musekit.databinding.FragmentNoteForkBinding
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
        setupPresets()
        setupPlayer()
        setupListeners()
        setupSavePresetDialog()
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

    private fun setupPresets() {
        // Setup lists and adapter
        val presetsNames = mutableListOf(
            getString(R.string.default_preset),
        )
        val presetsDetails = mutableListOf(
            Note(440, Notes.A, 4),
        )
        val savedPresets = PresetsManager.getPresets(requireContext())
        savedPresets.forEach {
            val name = it.name
            val note = Note(it.pitch, Notes.fromSemitones(it.semitones), it.octave)

            presetsNames.add(name)
            presetsDetails.add(note)
        }
        val presetsAdapter = PresetsAdapter(requireContext(), presetsNames)

        // Attach adapter to view
        binding.presetsPicker.setAdapter(presetsAdapter)
        binding.presetsPicker.setText(getString(R.string.default_preset), false)
        binding.presetsPicker.setOnItemClickListener { _, _, i, _ ->
            note.octave = presetsDetails[i].octave
            note.pitch = presetsDetails[i].pitch
            note.note = presetsDetails[i].note
            refreshTextViews()
            if (playing) {
                restartPlayer()
            }
        }
    }

    private fun setupPlayer() {
        sampleRate = AudioTrack.getNativeOutputSampleRate(AudioTrack.MODE_STATIC)
        player = AudioTrack.Builder()
            .setTransferMode(AudioTrack.MODE_STATIC)
            .setBufferSizeInBytes(sampleRate * 2)
            .build()
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

    // TODO: Animate play/pause button
    private fun playSound() {
        if (player.playState != AudioTrack.PLAYSTATE_PLAYING) {
            val tone = createSineWave(note.getFrequency())

            player.write(tone, 0, tone.size)
            val loopEnd = player.bufferSizeInFrames
            player.setLoopPoints(0, loopEnd, -1)

            player.play()
        }
    }

    private fun createSineWave(frequency: Double): ShortArray {
        val samples = DoubleArray(sampleRate * 2)
        val buffer = ShortArray(sampleRate * 2)

        for (i in buffer.indices) {
            samples[i] = sin(i * 2 * Math.PI * frequency.toInt() / sampleRate)
            buffer[i] = (samples[i] * Short.MAX_VALUE).toInt().toShort()
        }

        return buffer
    }

    private fun stopSound() {
        player.stop()
        player.flush()
    }

    private fun setupSavePresetDialog() {
        binding.buttonSavePreset.setOnClickListener { showSavePresetDialog() }
    }

    private fun showSavePresetDialog() {
        val dialogBinding = DialogSavePresetBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.save_preset)
            .setView(dialogBinding.root)
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.save) { _, _ ->
                // Get preset details
                val preset = PresetsManager.Companion.Preset(
                    name = dialogBinding.presetName.text.toString(),
                    semitones = note.note.semitones,
                    octave = note.octave,
                    pitch = note.pitch
                )

                // Save preset
                PresetsManager.savePreset(preset, requireContext())

                // Refresh presets list
                setupPresets()
            }
            .create()

        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled = false

        // Disable "Save" button if text field is empty
        dialogBinding.presetName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.isEnabled =
                    p0?.isBlank() != true
            }
        })
    }
}