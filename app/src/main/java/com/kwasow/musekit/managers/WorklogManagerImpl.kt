package com.kwasow.musekit.managers

import com.kwasow.musekit.room.AppDatabase
import com.kwasow.musekit.room.PracticeSession
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class WorklogManagerImpl(database: AppDatabase) : WorklogManager {
    // ====== Fields
    private val practiceSessionDao = database.practiceSessionDao()

    // ====== Interface methods
    override suspend fun getPracticeSessions(): List<PracticeSession> {
        return practiceSessionDao.getAll()
    }

    override suspend fun addWorklogEntry(startTime: LocalDateTime, endTime: LocalDateTime) {
        val seconds = ChronoUnit.SECONDS.between(startTime, endTime)


    }
}
