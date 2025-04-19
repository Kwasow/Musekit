package com.kwasow.musekit.ui.screens.fork

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.kwasow.musekit.R
import com.kwasow.musekit.adapters.PresetsAdapter
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.data.Notes
import com.kwasow.musekit.managers.PermissionManager
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.PresetsManager

class NoteForkScreenViewModel(
    private val applicationContext: Context,
    private val permissionManager: PermissionManager,
    private val preferencesManager: PreferencesManager,
    private val presetsManager: PresetsManager,
) : ViewModel() {
    // ======= Public methods
    fun getNoteForkMode(): Int = preferencesManager.getNoteForkMode()

    fun setNoteForkMode(value: Int) = preferencesManager.setNoteForkMode(value)

    fun launchPermissionSettings() = permissionManager.openPermissionSettings()

    fun requestMicrophonePermission(
        fragment: Fragment,
        callback: (Boolean) -> Unit,
    ) = permissionManager.requestMicrophonePermission(fragment, callback)

    fun getPitch(): Int = preferencesManager.getAutomaticTunerPitch()

    fun setPitch(value: Int) = preferencesManager.setAutomaticTunerPitch(value)

    fun getPresets(): List<Pair<String, Note>> {
        val presets =
            mutableListOf(
                applicationContext.getString(R.string.default_preset) to Note(440, Notes.A, 4),
            )
        presetsManager.getPresets().forEach {
            val name = it.name
            val note = Note(it.pitch, Notes.fromSemitones(it.semitones), it.octave)

            presets.add(name to note)
        }

        return presets
    }

    fun buildPresetsAdapter(
        presets: List<Pair<String, Note>>,
        onDelete: (String) -> Unit,
    ): PresetsAdapter =
        PresetsAdapter(
            applicationContext,
            presets.map { it.first },
        ) { presetName -> onDelete(presetName) }

    fun getNotationStyle() = preferencesManager.getNotationStyle()

    fun getSuperscriptedNote(note: Note): SpannableStringBuilder =
        note.getSuperscripted(
            applicationContext,
            getNotationStyle(),
        )
}
