package com.kwasow.musekit.data

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.kwasow.musekit.R

enum class MetronomeSounds(
    val id: Int,
    @RawRes val resourceId: Int,
    @StringRes val resourceNameId: Int,
) {
    Default(0, R.raw.metronome_click, R.string.metronome_sound_default),
    Beep(1, R.raw.metronome_beep, R.string.metronome_sound_beep),
    Ding(2, R.raw.metronome_ding, R.string.metronome_sound_ding),
    Wood(3, R.raw.metronome_wood, R.string.metronome_sound_wood),
    None(4, -1, R.string.metronome_sound_none),
    ;

    companion object {
        fun valueOf(id: Int): MetronomeSounds? {
            return when (id) {
                0 -> Default
                1 -> Beep
                2 -> Ding
                3 -> Wood
                4 -> None
                else -> null
            }
        }
    }
}
