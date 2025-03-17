package com.kwasow.musekit.managers

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors

class ThemeManagerImpl(
    applicationContext: Context,
    private val preferencesManager: PreferencesManager,
): ThemeManager {
    // ====== Constructors
    init {
        DynamicColors.applyToActivitiesIfAvailable(applicationContext as Application)
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
