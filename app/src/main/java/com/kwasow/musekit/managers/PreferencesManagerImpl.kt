package com.kwasow.musekit.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.kwasow.musekit.data.NotationStyle

class PreferencesManagerImpl(context: Context) : PreferencesManager {
    // ====== Fields
    companion object {
        // Shared preferences keys
        private const val PREFERENCES_FILE = "com.kwasow.musekit.ApplicationSharedPreferences"

        private const val KEY_NIGHT_MODE = "DarkTheme"
        private const val KEY_NOTE_FORK_MODE = "NoteForkMode"
        private const val KEY_AUTO_TUNER_PITCH = "AutoTunerPitch"
        private const val KEY_METRONOME_BPM = "MetronomeBPM"
        private const val KEY_NOTATION_STYLE = "NotationStyle"
    }

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(
            PREFERENCES_FILE,
            Context.MODE_PRIVATE,
        )

    // ====== Public methods
    override fun setNightMode(value: Int) = setInt(KEY_NIGHT_MODE, value)

    override fun getNightMode(default: Int): Int = getInt(KEY_NIGHT_MODE, default)

    override fun setNoteForkMode(value: Int) = setInt(KEY_NOTE_FORK_MODE, value)

    override fun getNoteForkMode(): Int = getInt(KEY_NOTE_FORK_MODE, 0)

    override fun setAutomaticTunerPitch(value: Int) = setInt(KEY_AUTO_TUNER_PITCH, value)

    override fun getAutomaticTunerPitch(): Int = getInt(KEY_AUTO_TUNER_PITCH, 440)

    override fun setMetronomeBPM(value: Int) = setInt(KEY_METRONOME_BPM, value)

    override fun getMetronomeBPM(): Int = getInt(KEY_METRONOME_BPM, 60)

    override fun setNotationStyle(value: NotationStyle) = setInt(KEY_NOTATION_STYLE, value.id)

    override fun getNotationStyle(): NotationStyle {
        val id = sharedPreferences.getInt(KEY_NOTATION_STYLE, NotationStyle.English.id)
        val style = NotationStyle.valueOf(id)

        return style ?: NotationStyle.English
    }

    // ====== Private methods
    private fun setInt(
        key: String,
        value: Int,
    ) = sharedPreferences.edit {
        putInt(key, value)
        apply()
    }

    private fun getInt(
        key: String,
        default: Int,
    ): Int = sharedPreferences.getInt(key, default)
}
