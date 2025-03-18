package com.kwasow.musekit.models

import androidx.lifecycle.ViewModel
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.managers.PreferencesManager

class SettingsFragmentViewModel(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    // ====== Public methods
    fun setNotationStyle(style: NotationStyle) = preferencesManager.setNotationStyle(style)

    fun getNotationStyle(): NotationStyle = preferencesManager.getNotationStyle()
}
