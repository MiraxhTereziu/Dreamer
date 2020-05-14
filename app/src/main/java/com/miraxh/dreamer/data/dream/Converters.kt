package com.miraxh.dreamer.data.dream

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(list: MutableList<String>): String {
        var toRtn = ""
        for(i in 0 until list.size){
            toRtn += if(i == list.size-1)
                list[i]
            else
                "${list[i]},"
        }
        return toRtn
    }

    @TypeConverter
    fun toList(list: String): MutableList<String> {
        var toRtn = mutableListOf<String>()

        toRtn = if(list.isBlank())
            emptyList<String>().toMutableList()
        else
            list.split(",").toMutableList()
        return toRtn
    }
}
