package com.miraxh.dreamer.data.dream

import android.util.Log
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(list: MutableList<String>): String {
        var toRtn = ""
        for(i in 0 until list.size){
            if(i == list.size-1)
                toRtn += list[i]
            else
                toRtn += "${list[i]},"
        }
        return toRtn
    }

    @TypeConverter
    fun toList(list: String): MutableList<String> {
        var toRtn = mutableListOf<String>()

        if(list.isBlank())
            toRtn = emptyList<String>().toMutableList()
        else
            toRtn = list.split(",").toMutableList()
        return toRtn
    }
}
