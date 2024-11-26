package com.kwasow.musekit.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object MusekitPreferences {
    // ====== Fields
    private lateinit var sharedPreferences: SharedPreferences

    // Shared preferences keys
    private const val PREFERENCES_FILE = "com.kwasow.musekit.ApplicationSharedPreferences"

    private const val KEY_NIGHT_MODE = "DarkTheme"
    private const val KEY_NOTE_FORK_MODE = "NoteForkMode"

    // ====== Constructors
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            PREFERENCES_FILE,
            Context.MODE_PRIVATE
        )
    }

    // ====== Public methods
    var nightMode: Int
        set(value) {
            sharedPreferences.edit {
                putInt(KEY_NIGHT_MODE, value)
                apply()
            }
        }
        get() {
            return sharedPreferences.getInt(KEY_NIGHT_MODE, ThemeUtils.getDefaultNightMode())
        }

    var noteForkMode: Int
        set(value) {
            sharedPreferences.edit {
                putInt(KEY_NOTE_FORK_MODE, value)
                apply()
            }
        }
        get() {
            return sharedPreferences.getInt(KEY_NOTE_FORK_MODE, 0)
        }

}