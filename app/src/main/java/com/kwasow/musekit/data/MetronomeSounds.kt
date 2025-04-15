package com.kwasow.musekit.data

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.kwasow.musekit.R

enum class MetronomeSounds(
    @RawRes val resourceId: Int,
    @StringRes val resourceNameId: Int,
) {
    Default(R.raw.metronome_click, R.string.metronome_sound_default),
    Beep(R.raw.metronome_beep, R.string.metronome_sound_beep),
    Ding(R.raw.metronome_ding, R.string.metronome_sound_ding),
    Wood(R.raw.metronome_wood, R.string.metronome_sound_wood),
    None(-1, R.string.metronome_sound_none),
}