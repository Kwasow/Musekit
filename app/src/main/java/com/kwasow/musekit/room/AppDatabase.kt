package com.kwasow.musekit.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PracticeSession::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun practiceSessionDao(): PracticeSessionDao
}
