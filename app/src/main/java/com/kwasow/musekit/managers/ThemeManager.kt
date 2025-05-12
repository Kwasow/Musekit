package com.kwasow.musekit.managers

import androidx.lifecycle.MutableLiveData

interface ThemeManager {
    // ====== Fields
    val nightMode: MutableLiveData<Int>

    // ====== Methods
    fun getDefaultNightMode(): Int

    fun setNightMode(newMode: Int)
}
