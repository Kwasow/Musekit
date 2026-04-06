package com.kwasow.musekit.ui.screens.fork

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwasow.musekit.R
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Notes
import com.kwasow.musekit.managers.PermissionManager
import com.kwasow.musekit.managers.PitchPlayerManager
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.PresetsManager
import com.kwasow.musekit.room.Preset
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NoteForkScreenViewModel(
    private val applicationContext: Context,
    private val permissionManager: PermissionManager,
    private val pitchPlayerManager: PitchPlayerManager,
    private val preferencesManager: PreferencesManager,
    private val presetsManager: PresetsManager,
) : ViewModel() {
    // ====== Fields
    val defaultPreset =
        with(Note()) {
            Preset(
                id = -1,
                name = applicationContext.getString(R.string.default_preset),
                semitones = this.note.semitones,
                octave = this.octave,
                pitch = this.pitch,
            )
        }

    val presets: MutableLiveData<List<Preset>> = MutableLiveData(emptyList())
    var currentNote by mutableStateOf(
        value = Note(),
        policy = neverEqualPolicy(),
    )
        private set
    val isPlaying = MutableLiveData(false)

    val noteForkMode = runBlocking { preferencesManager.noteForkMode.first() }
    val automaticTunerPitch =
        preferencesManager.automaticTunerPitch.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            440,
        )
    val notationStyle =
        preferencesManager.notationStyle.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            NotationStyle.English,
        )

    // ====== Constructors
    init {
        viewModelScope.launch {
            refreshPresets()
        }

        setNote(currentNote)
    }

    // ====== Public methods
    fun launchPermissionSettings() = permissionManager.openPermissionSettings()

    fun setNoteForkMode(mode: Int) =
        viewModelScope.launch {
            preferencesManager.setNoteForkMode(mode)
        }

    fun setAutomaticTunerPitch(value: Int) =
        viewModelScope.launch {
            preferencesManager.setAutomaticTunerPitch(value)
        }

    fun getSuperscriptedNote(
        note: Note,
        style: NotationStyle,
    ): AnnotatedString = note.getSuperscripted(applicationContext, style)

    fun setNote(note: Note) {
        currentNote = note

        pitchPlayerManager.frequency = note.getFrequency()
    }

    fun setNoteFromPreset(preset: Preset) =
        setNote(
            Note(
                pitch = preset.pitch,
                note = Notes.fromSemitones(preset.semitones),
                octave = preset.octave,
            ),
        )

    suspend fun addPreset(
        name: String,
        note: Note,
    ): Preset? {
        val preset =
            Preset(
                name = name,
                semitones = note.note.semitones,
                octave = note.octave,
                pitch = note.pitch,
            )

        val id = presetsManager.savePreset(preset) ?: return null
        refreshPresets()

        return preset.copy(id = id)
    }

    fun removePreset(id: Long) {
        viewModelScope.launch {
            presetsManager.removePreset(id)
            refreshPresets()
        }
    }

    fun startStopNote() {
        if (isPlaying.value == true) {
            stopNote()
        } else {
            startNote()
        }
    }

    fun startNote() {
        pitchPlayerManager.play()
        isPlaying.postValue(true)
    }

    fun stopNote() {
        pitchPlayerManager.stop()
        isPlaying.postValue(false)
    }

    // ====== Private methods
    private suspend fun refreshPresets() {
        val newPresets = presetsManager.getPresets()
        presets.postValue(newPresets)
    }
}
