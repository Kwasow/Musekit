package com.kwasow.musekit.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Preset(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "semitones") val semitones: Int,
    @ColumnInfo(name = "octave") val octave: Int,
    @ColumnInfo(name = "pitch") val pitch: Int,
)
