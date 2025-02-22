package com.kwasow.musekit.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.kwasow.musekit.data.NotationStyle

object MusekitPreferences {

    // ====== Fields
    private lateinit var sharedPreferences: SharedPreferences

    // Shared preferences keys
    private const val PREFERENCES_FILE = "com.kwasow.musekit.ApplicationSharedPreferences"

    private const val KEY_NIGHT_MODE = "DarkTheme"
    private const val KEY_NOTE_FORK_MODE = "NoteForkMode"
    private const val KEY_AUTO_TUNER_PITCH = "AutoTunerPitch"
    private const val KEY_METRONOME_BPM = "MetronomeBPM"
    private const val KEY_NOTATION_STYLE = "NotationStyle"

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
        get() = sharedPreferences.getInt(KEY_NOTE_FORK_MODE, 0)

    var automaticTunerPitch: Int
        set(value) {
            sharedPreferences.edit {
                putInt(KEY_AUTO_TUNER_PITCH, value)
                apply()
            }
        }
        get() = sharedPreferences.getInt(KEY_AUTO_TUNER_PITCH, 440)

    var metronomeBPM: Int
        set(value) {
            sharedPreferences.edit {
                putInt(KEY_METRONOME_BPM, value)
                apply()
            }
        }
        get() = sharedPreferences.getInt(KEY_METRONOME_BPM, 60)

    var notationStyle: NotationStyle
        set(value) {
            sharedPreferences.edit {
                putInt(KEY_NOTATION_STYLE, value.id)
                apply()
            }
        }
        get() {
            val id = sharedPreferences.getInt(KEY_NOTATION_STYLE, NotationStyle.English.id)
            val style = NotationStyle.valueOf(id)

            return style ?: NotationStyle.English
        }
}
