package com.kwasow.musekit.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.kwasow.musekit.R

@RequiresApi(Build.VERSION_CODES.Q)
enum class NightMode(
    val id: Int,
    val value: Int,
    @StringRes val nameId: Int,
) {
    DARK(0, AppCompatDelegate.MODE_NIGHT_YES, R.string.theme_dark),
    LIGHT(1, AppCompatDelegate.MODE_NIGHT_NO, R.string.theme_light),
    SYSTEM(2, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.theme_follow_system),
}

@RequiresApi(
    Build.VERSION_CODES.Q,
    1
)
enum class NightMode(
    val id: Int,
    val value: Int,
    @StringRes val nameId: Int,
) {
    DARK(0, AppCompatDelegate.MODE_NIGHT_YES, R.string.theme_dark),
    LIGHT(1, AppCompatDelegate.MODE_NIGHT_NO, R.string.theme_light),
    SYSTEM(2, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.theme_follow_system),
}
