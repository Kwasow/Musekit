package com.kwasow.musekit.managers

import com.kwasow.musekit.room.PracticeSession
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface WorklogManager {
    // ====== Methods
    val practiceSessions: Flow<List<PracticeSession>>

    suspend fun addWorklogEntry(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    )
}
