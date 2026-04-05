package com.kwasow.musekit.room

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun localDateToString(date: LocalDate): String = date.toString()

    @TypeConverter
    fun stringToLocalDate(string: String): LocalDate = LocalDate.parse(string)
}
