package com.kwasow.musekit.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDate

@Dao
interface PracticeSessionDao {
    @Query("SELECT * FROM PracticeSession")
    suspend fun getAll(): List<PracticeSession>

    @Query("SELECT * FROM PracticeSession WHERE date LIKE :date LIMIT 1")
    suspend fun get(date: LocalDate): PracticeSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg practiceSessions: PracticeSession)

    @Delete
    suspend fun delete(practiceSession: PracticeSession)
}
