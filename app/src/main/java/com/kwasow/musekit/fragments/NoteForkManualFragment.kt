package com.kwasow.musekit.fragments

import android.graphics.drawable.AnimatedVectorDrawable
import android.media.AudioTrack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.kwasow.musekit.R
import com.kwasow.musekit.adapters.PresetsAdapter
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Notes
import com.kwasow.musekit.databinding.FragmentNoteForkManualBinding
import com.kwasow.musekit.dialogs.PresetDeleteDialog
import com.kwasow.musekit.dialogs.PresetSaveDialogFragment
import com.kwasow.musekit.utils.PresetsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sin
import kotlin.properties.Delegates

class NoteForkManualFragment : Fragment() {

    // ====== Fields
    private lateinit var buttonNoteDown: MaterialButton
    private lateinit var buttonNoteUp: MaterialButton
    private lateinit var buttonPitchDown: MaterialButton
    private lateinit var buttonPitchUp: MaterialButton
    private lateinit var buttonStartStop: MaterialButton
    private lateinit var textPitch: AppCompatTextView
    private lateinit var textNote: AppCompatTextView
    private lateinit var buttonSavePreset: MaterialButton
    private lateinit var presetsPicker: AutoCompleteTextView

    private var note: Note = Note()

    private lateinit var player: AudioTrack
    private var playing = false
    private var sampleRate by Delegates.notNull<Int>()

    // ====== Interface methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNoteForkManualBinding.inflate(inflater)

        buttonNoteDown = binding.buttonNoteDown
        buttonNoteUp = binding.buttonNoteUp
        buttonPitchDown = binding.buttonPitchDown
        buttonPitchUp = binding.buttonPitchUp
        buttonStartStop = binding.buttonStartStop
        textPitch = binding.textPitch
        textNote = binding.textNote
        buttonSavePreset = binding.buttonSavePreset
        presetsPicker = binding.presetsPicker

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setupPlayer()
        setupPresets()
        setupButtons()
        setupSavePresetDialog()
    }

    override fun onResume() {
        super.onResume()

        refreshState()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::player.isInitialized) {
            player.release()
        }
    }

    // ====== Private methods
    private fun refreshState() {
        textPitch.text = getString(R.string.pitch_placeholder, note.pitch)
        textNote.text = note.getSuperscripted(requireContext())

        if (playing) {
            lifecycleScope.launch {
                reloadTone()
            }
        }
    }

    private fun setupPresets() {
        // Setup lists and adapter
        val presets = mutableListOf(
            getString(R.string.default_preset) to Note(440, Notes.A, 4)
        )
        PresetsManager.getPresets(requireContext()).forEach {
            val name = it.name
            val note = Note(it.pitch, Notes.fromSemitones(it.semitones), it.octave)

            presets.add(name to note)
        }

        // Attach adapter to view
        val presetsAdapter = PresetsAdapter(
            requireContext(),
            presets.map { it.first }
        ) { presetName -> showDeletePresetDialog(presetName) }

        presetsPicker.setAdapter(presetsAdapter)
        presetsPicker.setText(getString(R.string.default_preset), false)
        presetsPicker.setOnItemClickListener { _, _, i, _ ->
            note.octave = presets[i].second.octave
            note.pitch = presets[i].second.pitch
            note.note = presets[i].second.note
            refreshState()
        }

        // Check if we can select a preset
        selectPreset(presets)
    }

    private fun setupPlayer() {
        sampleRate = AudioTrack.getNativeOutputSampleRate(AudioTrack.MODE_STATIC)
        player = AudioTrack.Builder()
            .setTransferMode(AudioTrack.MODE_STATIC)
            .setBufferSizeInBytes(sampleRate * 2)
            .build()
    }

    private fun setupButtons() {
        buttonNoteUp.setOnClickListener {
            note.up()
            refreshState()
        }

        buttonNoteDown.setOnClickListener {
            note.down()
            refreshState()
        }

        buttonPitchUp.setOnClickListener {
            note.pitch += 1
            refreshState()
        }

        buttonPitchDown.setOnClickListener {
            note.pitch -= 1
            refreshState()
        }

        buttonStartStop.setOnClickListener {
            if (!playing) {
                playing = true
                playSound()
                buttonStartStop.apply {
                    setIconResource(R.drawable.anim_play_to_pause)
                    (icon as AnimatedVectorDrawable).start()
                }
            } else {
                playing = false
                stopSound()
                buttonStartStop.apply {
                    setIconResource(R.drawable.anim_pause_to_play)
                    (icon as AnimatedVectorDrawable).start()
                }
            }
        }
    }

    private suspend fun reloadTone() {
        val tone = createSineWave(note.getFrequency())

        player.write(tone, 0, tone.size)
        val loopEnd = player.bufferSizeInFrames
        player.setLoopPoints(0, loopEnd, -1)
    }

    private fun playSound() {
        if (player.playState != AudioTrack.PLAYSTATE_PLAYING && playing) {
            lifecycleScope.launch {
                reloadTone()
                player.setVolume(0f)
                player.play()
                player.setVolume(1f)
            }
        }
    }

    private suspend fun createSineWave(frequency: Double): ShortArray {
        return withContext(Dispatchers.Main) {
            val samples = DoubleArray(sampleRate * 2)
            val buffer = ShortArray(sampleRate * 2)

            for (i in buffer.indices) {
                samples[i] = sin(i * 2 * Math.PI * frequency.toInt() / sampleRate)
                buffer[i] = (samples[i] * Short.MAX_VALUE).toInt().toShort()
            }

            return@withContext buffer
        }
    }

    private fun stopSound() {
        if (player.state == AudioTrack.STATE_INITIALIZED) {
            player.stop()
        }
    }

    private fun setupSavePresetDialog() {
        buttonSavePreset.setOnClickListener { showSavePresetDialog() }
    }

    private fun showSavePresetDialog() =
        PresetSaveDialogFragment(note) { setupPresets() }.show(
            childFragmentManager,
            PresetSaveDialogFragment.TAG
        )

    private fun showDeletePresetDialog(presetName: String) =
        PresetDeleteDialog(presetName) { setupPresets() }.show(
            childFragmentManager,
            PresetDeleteDialog.TAG
        )

    private fun selectPreset(presets: List<Pair<String, Note>>) {
        val selected = presets.find { it.second == note }
        val name = selected?.first ?: presets[0].first

        presetsPicker.setText(name, false)
    }
}
