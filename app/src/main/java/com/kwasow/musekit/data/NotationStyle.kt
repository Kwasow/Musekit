package com.kwasow.musekit.data

enum class NotationStyle(val id: Int) {
    English(0),
    German(1),
    FixedDo(2);

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
