package com.example.finance.database.converters

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class Converters {
    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    @TypeConverter
    fun toDate(string: String?): Date? {
        return string?.let { dateFormat.parse(it) }
    }
}
