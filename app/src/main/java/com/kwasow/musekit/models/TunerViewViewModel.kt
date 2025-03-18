package com.kwasow.musekit.models

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.lifecycle.ViewModel
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.managers.PreferencesManager

class TunerViewViewModel(
    private val applicationContext: Context,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ======= Public methods
    fun getSuperscriptedNote(note: Note): SpannableStringBuilder =
        note.getSuperscripted(applicationContext, preferencesManager.getNotationStyle())
}
