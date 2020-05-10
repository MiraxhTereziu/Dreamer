package com.miraxh.dreamer.data.dream

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(list: MutableList<String>): String {
        var toRtn = ""
        list.forEach {
            toRtn += "$it,"
        }
        return toRtn
    }

    @TypeConverter
    fun toList(list: String): MutableList<String> {
        return list.split(",").toMutableList()
    }
}
