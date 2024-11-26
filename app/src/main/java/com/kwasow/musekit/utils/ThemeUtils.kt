package com.kwasow.musekit.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.android.material.color.DynamicColors
import com.kwasow.musekit.R

object ThemeUtils {
    // ====== Public methods
    fun init(application: Application) {
        DynamicColors.applyToActivitiesIfAvailable(application)
        AppCompatDelegate.setDefaultNightMode(getNightMode())
    }

    fun getDefaultNightMode(): Int {
        return if (Build.VERSION.SDK_INT >= 29) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    fun getNightMode(): Int =
        MusekitPreferences.nightMode

    fun setNightMode(nightMode: Int) {
        MusekitPreferences.nightMode = nightMode
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
