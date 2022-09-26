package com.kwasow.musekit

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.android.material.color.DynamicColors

// TODO: Light/Dark theme circles could be optimized (we don't need a separate file for the two of these)

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setupDynamicColors()
        setupNightMode()
    }

    private fun setupDynamicColors() {
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    private fun setupNightMode() {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.preferences_file_key),
            Context.MODE_PRIVATE
        )
        val defaultNightMode =
            if (Build.VERSION.SDK_INT >= 29) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        val nightMode = sharedPreferences.getInt(
            getString(R.string.preferences_night_mode),
            defaultNightMode
        )

        sharedPreferences.edit {
            putInt(
                getString(R.string.preferences_night_mode),
                nightMode
            )
            apply()
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
