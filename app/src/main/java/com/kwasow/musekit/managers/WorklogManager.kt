package com.kwasow.musekit.managers

import com.kwasow.musekit.room.PracticeSession
import java.time.LocalDate
import java.time.LocalDateTime

interface WorklogManager {
    // ====== Methods
    suspend fun getPracticeSessions(): List<PracticeSession>

    suspend fun addWorklogEntry(startTime: LocalDateTime, endTime: LocalDateTime)
}
