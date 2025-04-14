package com.kwasow.musekit.data

import androidx.annotation.StringRes
import com.kwasow.musekit.R

enum class NotationStyle(
    val id: Int,
    @StringRes val nameId: Int,
    val noteNames: List<String>,
) {
    English(
        0,
        R.string.notation_style_english,
        listOf("A", "A♯/B♭", "B", "C", "C♯/D♭", "D", "D♯/E♭", "E", "F", "F♯/G♭", "G", "G♯/A♭"),
    ),
    German(
        1,
        R.string.notation_style_german,
        listOf("A", "A♯/B", "H", "C", "C♯/D♭", "D", "D♯/E♭", "E", "F", "F♯/G♭", "G", "G♯/A♭"),
    ),
    FixedDo(
        2,
        R.string.notation_style_fixed_do,
        listOf(
            "La",
            "La♯/Si♭",
            "Si",
            "Do",
            "Do♯/Re♭",
            "Re",
            "Re♯/Mi♭",
            "Mi",
            "Fa",
            "Fa♯/Sol♭",
            "Sol",
            "Sol♯/La♭",
        ),
    ),
    ;

    companion object {
        fun valueOf(id: Int): NotationStyle? {
            return when (id) {
                0 -> English
                1 -> German
                2 -> FixedDo
                else -> null
            }
        }
    }
}
