package com.kwasow.musekit.managers

interface PracticeHistory {
    // ====== Methods
    fun addSession(length: Long)

    fun getHistory()
}
