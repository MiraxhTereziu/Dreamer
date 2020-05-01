package com.miraxh.dreamer.data.dream

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(tagsList: List<String>): String {
        var toRtn = ""
        tagsList.forEach {
            toRtn += "$it,"
        }
        return toRtn
    }

    @TypeConverter
    fun toList(tagsString: String): List<String> {
        return tagsString.split(",").toList()
    }
}
