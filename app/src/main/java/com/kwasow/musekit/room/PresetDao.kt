package com.kwasow.musekit.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PresetDao {
    @Query("SELECT * FROM Preset")
    suspend fun getAll(): List<Preset>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg presets: Preset)

    @Query("DELETE FROM Preset WHERE id = :id")
    suspend fun deleteById(id: Long)
}
