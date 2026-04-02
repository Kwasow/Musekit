package com.kwasow.musekit.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kwasow.musekit.R

enum class TopLevelRoutes(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    NoteFork(
        label = R.string.tuning,
        icon = R.drawable.ic_note_fork,
    ),
    Metronome(
        label = R.string.metronome,
        icon = R.drawable.ic_metronome,
    ),
    Settings(
        label = R.string.settings,
        icon = R.drawable.ic_settings,
    ),
}
