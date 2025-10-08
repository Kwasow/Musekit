package com.kwasow.musekit.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kwasow.musekit.R
import com.kwasow.musekit.data.MetronomeSounds
import kotlinx.serialization.Serializable

enum class Routes(
    @StringRes val titleId: Int,
    val route: Any,
    @DrawableRes val iconId: Int
) {
    NOTE_FORK(
        R.string.tuning,
        NoteFork,
        R.drawable.ic_note_fork,
    ),
    METRONOME(
        R.string.metronome,
        MetronomeSounds,
        R.drawable.ic_metronome,
    ),
    SETTINGS(
        R.string.settings,
        Settings,
        R.drawable.ic_settings,
    ),
}

@Serializable
object NoteFork

@Serializable
object Metronome

@Serializable
object Settings
