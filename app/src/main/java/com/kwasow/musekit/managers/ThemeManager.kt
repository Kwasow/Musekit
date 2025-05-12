package com.kwasow.musekit.managers

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow

interface ThemeManager {
    // ====== Fields
    val nightMode: MutableLiveData<Int>

    // ====== Methods
    fun getDefaultNightMode(): Int

    fun setNightMode(newMode: Int)
}
