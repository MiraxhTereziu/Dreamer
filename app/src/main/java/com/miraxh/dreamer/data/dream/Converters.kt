package com.miraxh.dreamer.data.dream

import android.util.Log
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(tagsList: MutableList<String>): String {
        var toRtn = ""
        tagsList.forEach {
            toRtn += "$it,"
        }
        return toRtn
    }

    @TypeConverter
    fun toList(tagsString: String): MutableList<String> {
        return tagsString.split(",").toMutableList()
    }
}
