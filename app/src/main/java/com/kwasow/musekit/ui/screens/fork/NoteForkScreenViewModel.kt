package com.kwasow.musekit.ui.screens.fork

import android.content.Context
import android.media.AudioTrack
import android.text.SpannableStringBuilder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.kwasow.musekit.R
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Notes
import com.kwasow.musekit.data.Preset
import com.kwasow.musekit.managers.PermissionManager
import com.kwasow.musekit.managers.PitchPlayerManager
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.PresetsManager
import kotlin.math.sin

class NoteForkScreenViewModel(
    private val applicationContext: Context,
    private val permissionManager: PermissionManager,
    private val pitchPlayerManager: PitchPlayerManager,
    private val preferencesManager: PreferencesManager,
    private val presetsManager: PresetsManager,
) : ViewModel() {
    // ======= Classes
    enum class Dialog {
        SAVE_PRESET,
        REMOVE_PRESET,
        NONE,
    }

    // ======= Fields
    val defaultPreset =
        applicationContext.getString(R.string.default_preset) to Note(440, Notes.A, 4)

    var presets by mutableStateOf<List<Pair<String, Note>>>(emptyList())
    var currentNote by mutableStateOf(
        value = Note(),
        policy = neverEqualPolicy(),
    )
        private set

    var currentPreset by mutableStateOf(defaultPreset.first)
    var currentDialog by mutableStateOf(Dialog.NONE)
    var currentRemovePresetName by mutableStateOf("")
    var isPlaying by mutableStateOf(false)

    var noteForkMode: Int
        get() = preferencesManager.getNoteForkMode()
        set(value) = preferencesManager.setNoteForkMode(value)

    // ======= Constructors
    init {
        refreshPresets()
        setNote(currentNote)
    }

    // ======= Public methods
    fun launchPermissionSettings() = permissionManager.openPermissionSettings()

    fun requestMicrophonePermission(
        fragment: Fragment,
        callback: (Boolean) -> Unit,
    ) = permissionManager.requestMicrophonePermission(fragment, callback)

    fun getPitch(): Int = preferencesManager.getAutomaticTunerPitch()

    fun setPitch(value: Int) = preferencesManager.setAutomaticTunerPitch(value)

    fun getSuperscriptedNote(note: Note): SpannableStringBuilder =
        note.getSuperscripted(
            applicationContext,
            getNotationStyle(),
        )

    fun setNote(note: Note) {
        currentNote = note

        pitchPlayerManager.frequency = note.getFrequency()
    }

    fun addPreset(name: String, note: Note) {
        val preset = Preset(
            name = name,
            semitones = note.note.semitones,
            octave = note.octave,
            pitch = note.pitch,
        )

        presetsManager.savePreset(preset)
        currentPreset = name

        refreshPresets()
    }

    fun removePreset(name: String) {
        currentRemovePresetName = name
        currentDialog = Dialog.REMOVE_PRESET
    }

    fun confirmRemovePreset() {
        presetsManager.removePreset(currentRemovePresetName)
        if (currentPreset == currentRemovePresetName) {
            currentPreset = defaultPreset.first
        }

        refreshPresets()
        closeDialog()
    }

    fun closeDialog() {
        currentDialog = Dialog.NONE
    }

    fun startStopNote() {
        if (isPlaying) {
            pitchPlayerManager.stop()
        } else {
            pitchPlayerManager.play()
        }

        isPlaying = !isPlaying
    }

    // ====== Private methods
    private fun getNotationStyle() = preferencesManager.getNotationStyle()

    private fun refreshPresets() {
        val newPresets = mutableListOf(defaultPreset)
        presetsManager.getPresets().forEach {
            val name = it.name
            val note = Note(it.pitch, Notes.fromSemitones(it.semitones), it.octave)

            newPresets.add(name to note)
        }

        presets = newPresets
    }
}
