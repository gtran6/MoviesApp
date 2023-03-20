package com.example.moviesapp.model

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromListInt(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toListInt(string: String): List<Int> {
        return string.split(",").map { it.toInt() }
    }
}