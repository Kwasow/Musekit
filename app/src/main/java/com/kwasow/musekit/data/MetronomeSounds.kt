package com.kwasow.musekit.data

import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.kwasow.musekit.R

enum class MetronomeSounds(
    val id: Int,
    @RawRes private val resourceId44: Int,
    @RawRes private val resourceId48: Int,
    @StringRes val resourceNameId: Int,
) {
    Default(
        0,
        R.raw.metronome_click_44,
        R.raw.metronome_click_48,
        R.string.metronome_sound_default,
    ),
    Beep(1, R.raw.metronome_beep_44, R.raw.metronome_beep_48, R.string.metronome_sound_beep),
    Ding(2, R.raw.metronome_ding_44, R.raw.metronome_ding_48, R.string.metronome_sound_ding),
    Wood(3, R.raw.metronome_wood_44, R.raw.metronome_wood_48, R.string.metronome_sound_wood),
    None(4, -1, -1, R.string.metronome_sound_none),
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

    fun getResourceId(): Int {
        val preferredSampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC)

        return if (preferredSampleRate == 44100) {
            Log.d("Sample rate", "44100")
            resourceId44
        } else {
            Log.d("Sample rate", "48000")
            resourceId48
        }
    }
}
