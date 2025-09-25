package com.kwasow.musekit.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface PracticeSessionDao {
    @Query("SELECT * FROM PracticeSession")
    fun getAll(): List<PracticeSession>

    @Query("SELECT * FROM PracticeSession WHERE date LIKE :date LIMIT 1")
    fun getByDate(date: LocalDate): PracticeSession

    @Insert
    fun insertAll(vararg practiceSessions: PracticeSession)

    @Delete
    fun delete()
}