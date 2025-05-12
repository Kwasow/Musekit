package com.kwasow.musekit.managers

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData

class ThemeManagerImpl(private val preferencesManager: PreferencesManager) : ThemeManager {
    // ====== Fields
    override val nightMode = MutableLiveData(getNightMode())

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

    override fun setNightMode(newMode: Int) {
        preferencesManager.setNightMode(newMode)
        AppCompatDelegate.setDefaultNightMode(newMode)

        nightMode.postValue(newMode)
    }

    // ====== Private methods
    private fun getNightMode(): Int = preferencesManager.getNightMode(getDefaultNightMode())
}
