package com.kwasow.musekit.managers

import com.kwasow.musekit.room.PracticeSession
import java.time.LocalDate

interface WorklogManager {
    // ====== Methods
    fun getPracticeSessions(): List<PracticeSession>

    fun addWorklogEntry(startTime: LocalDate, endTime: LocalDate)
}