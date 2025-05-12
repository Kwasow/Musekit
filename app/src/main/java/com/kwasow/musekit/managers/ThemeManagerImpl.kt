package com.kwasow.musekit.managers

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class ThemeManagerImpl(private val preferencesManager: PreferencesManager) : ThemeManager {
    // ====== Constructors
    init {
        AppCompatDelegate.setDefaultNightMode(getNightMode())
    }

    // ====== Public methods
    override fun getDefaultNightMode(): Int {
        return if (Build.VERSION.SDK_INT >= 29) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    override fun getNightMode(): Int = preferencesManager.getNightMode(getDefaultNightMode())

    override fun setNightMode(nightMode: Int) {
        preferencesManager.setNightMode(nightMode)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
