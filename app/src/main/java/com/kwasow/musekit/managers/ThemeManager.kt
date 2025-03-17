package com.kwasow.musekit.managers

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors

interface ThemeManager {
    // ====== Methods
    fun getDefaultNightMode(): Int

    fun getNightMode(): Int

    fun setNightMode(nightMode: Int)
}
