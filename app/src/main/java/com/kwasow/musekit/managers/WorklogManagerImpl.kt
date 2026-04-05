package com.kwasow.musekit.managers

import com.kwasow.musekit.room.AppDatabase
import com.kwasow.musekit.room.PracticeSession
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class WorklogManagerImpl(
    database: AppDatabase,
) : WorklogManager {
    // ====== Fields
    private val practiceSessionDao = database.practiceSessionDao()

    // ====== Interface methods
    override suspend fun getPracticeSessions(): List<PracticeSession> = practiceSessionDao.getAll()

    override suspend fun addWorklogEntry(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ) {
        // 1. If you practice while daylight saving time changes, you'll probably loose/gain
        //    and hour of practice. I won't be testing or handling that. Maybe it is handled by
        //    the Java standard library.
        // 2. If you practice on your private jet going through timezones or even the international
        //    date line then yeah, sorry - I'm not handling that.

        if (startTime.dayOfMonth == endTime.dayOfMonth) {
            val today = startTime.toLocalDate()
            val seconds = ChronoUnit.SECONDS.between(startTime, endTime)

            addWorklogEntry(today, seconds)
        } else {
            val yesterday = startTime.toLocalDate()
            val today = endTime.toLocalDate()
            val midnight = LocalDateTime.of(today, LocalTime.of(0, 0))

            val secondsYesterday = ChronoUnit.SECONDS.between(startTime, midnight)
            val secondsToday = ChronoUnit.SECONDS.between(midnight, endTime)

            addWorklogEntry(yesterday, secondsYesterday)
            addWorklogEntry(today, secondsToday)
        }
    }

    // ====== Private methods
    private suspend fun addWorklogEntry(
        date: LocalDate,
        length: Long,
    ) {
        val existingEntry = practiceSessionDao.get(date) ?: PracticeSession(date = date, length = 0)
        val updatedEntry = existingEntry.copy(length = length)
        practiceSessionDao.insertAll(updatedEntry)
    }
}
