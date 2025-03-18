package com.kwasow.musekit.managers

interface ThemeManager {
    // ====== Methods
    fun getDefaultNightMode(): Int

    fun getNightMode(): Int

    fun setNightMode(nightMode: Int)
}
