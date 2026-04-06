package com.kwasow.musekit.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface PresetDao {
    @Query("SELECT * FROM Preset")
    suspend fun getAll(): List<Preset>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg presets: Preset)

    @Delete
    suspend fun deleteById(id: Long)
}
