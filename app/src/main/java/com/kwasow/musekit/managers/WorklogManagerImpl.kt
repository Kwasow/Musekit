package com.kwasow.musekit.managers

import com.kwasow.musekit.room.PracticeSession
import java.time.LocalDate

class WorklogManagerImpl : WorklogManager {
    // ====== Interface methods
    override fun getPracticeSessions(): List<PracticeSession> {
        return emptyList()
    }

    override fun addWorklogEntry(startTime: LocalDate, endTime: LocalDate) {
        return
    }
}
