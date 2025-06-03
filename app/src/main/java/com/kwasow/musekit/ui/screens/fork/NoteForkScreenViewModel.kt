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
import com.kwasow.musekit.data.Preset
import com.kwasow.musekit.managers.PermissionManager
import com.kwasow.musekit.managers.PitchPlayerManager
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.PresetsManager
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
    // ======= Fields
    val defaultPreset =
        applicationContext.getString(R.string.default_preset) to Note()

    val presets: MutableLiveData<List<Pair<String, Note>>> = MutableLiveData(emptyList())
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

    // ======= Constructors
    init {
        refreshPresets()
        setNote(currentNote)
    }

    // ======= Public methods
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

    fun addPreset(
        name: String,
        note: Note,
    ) {
        val preset =
            Preset(
                name = name,
                semitones = note.note.semitones,
                octave = note.octave,
                pitch = note.pitch,
            )

        presetsManager.savePreset(preset)
        refreshPresets()
    }

    fun removePreset(presetName: String) {
        presetsManager.removePreset(presetName)
        refreshPresets()
    }

    fun startStopNote() {
        if (isPlaying.value == true) {
            pitchPlayerManager.stop()
            isPlaying.postValue(false)
        } else {
            pitchPlayerManager.play()
            isPlaying.postValue(true)
        }
    }

    // ====== Private methods
    private fun refreshPresets() {
        val newPresets = mutableListOf(defaultPreset)
        presetsManager.getPresets().forEach {
            val name = it.name
            val note = Note(it.pitch, Notes.fromSemitones(it.semitones), it.octave)

            newPresets.add(name to note)
        }

        presets.postValue(newPresets)
    }
}
