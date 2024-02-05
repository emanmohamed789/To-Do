package com.example.todoapp

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConvertor {

    @TypeConverter
    fun convertToDate(dateTime: Long): Date {
        return Date(dateTime)
    }

    @TypeConverter
    fun convertFromDate(date: Date): Long {
        return date.time
    }
}