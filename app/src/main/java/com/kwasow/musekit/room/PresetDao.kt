package com.kwasow.musekit.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PresetDao {
    @Query("SELECT * FROM Preset")
    fun getAll(): Flow<List<Preset>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(preset: Preset): Long?

    @Query("DELETE FROM Preset WHERE id = :id")
    suspend fun deleteById(id: Long)
}
