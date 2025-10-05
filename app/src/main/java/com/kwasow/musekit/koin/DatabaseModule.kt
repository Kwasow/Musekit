package com.kwasow.musekit.koin

import androidx.room.Room
import com.kwasow.musekit.room.AppDatabase
import org.koin.dsl.module

val databaseModule =
    module {
        single<AppDatabase> {
            Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                "musekit-database"
            ).build()
        }
    }
