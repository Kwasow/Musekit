package com.kwasow.musekit.managers

import com.kwasow.musekit.room.PracticeSession
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface WorklogManager {
    // ====== Fields
    val practiceSessions: Flow<List<PracticeSession>>

    // ====== Methods
    suspend fun addWorklogEntry(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    )
}
