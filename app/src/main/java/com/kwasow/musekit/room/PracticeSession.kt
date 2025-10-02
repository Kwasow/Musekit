package com.kwasow.musekit.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class PracticeSession(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "length") val length: Long,
)
