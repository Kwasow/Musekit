package com.kwasow.musekit.room

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun localDateToString(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun stringToLocalDate(string: String): LocalDate {
        return LocalDate.parse(string)
    }
}
