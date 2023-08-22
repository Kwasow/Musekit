package com.kwasow.musekit.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.android.material.color.DynamicColors
import com.kwasow.musekit.R

class ThemeUtils {
    companion object {
        private lateinit var application: Application
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var sharedPreferencesNightMode: String

        fun init(application: Application) {
            this.application = application

            sharedPreferences = application.getSharedPreferences(
                application.getString(R.string.preferences_file_key),
                Context.MODE_PRIVATE
            )
            sharedPreferencesNightMode =
                application.getString(R.string.preferences_night_mode)

            // Create the setting if it does not exist
            if (!sharedPreferences.contains(sharedPreferencesNightMode)) {
                sharedPreferences.edit {
                    putInt(
                        sharedPreferencesNightMode,
                        getDefaultNightMode()
                    )
                    apply()
                }
            }

            DynamicColors.applyToActivitiesIfAvailable(application)
            AppCompatDelegate.setDefaultNightMode(getNightMode())
        }

        private fun getDefaultNightMode(): Int {
            return if (Build.VERSION.SDK_INT >= 29) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        }

        fun getNightMode(): Int {
            if (!::application.isInitialized) {
                throw Exception("ThemeUtils have not been initialized. Application context is null")
            }

            return sharedPreferences.getInt(sharedPreferencesNightMode, getDefaultNightMode())
        }

        fun setNightMode(nightMode: Int) {
            if (!::application.isInitialized) {
                throw Exception("ThemeUtils have not been initialized. Application context is null")
            }

            sharedPreferences.edit {
                putInt(
                    sharedPreferencesNightMode,
                    nightMode
                )
                apply()
            }

            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }
}
