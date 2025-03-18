package com.kwasow.musekit.models

import androidx.lifecycle.ViewModel
import com.kwasow.musekit.managers.PreferencesManager
import com.kwasow.musekit.managers.ThemeManager

class ThemeDialogViewModel(
    private val preferencesManager: PreferencesManager,
    private val themeManager: ThemeManager,
) : ViewModel() {
    // ====== Public methods
    fun getNightMode() = preferencesManager.getNightMode(themeManager.getDefaultNightMode())

    fun setNightMode(mode: Int) = preferencesManager.setNightMode(mode)
}
