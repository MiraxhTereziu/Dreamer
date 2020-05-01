package com.miraxh.dreamer.data.dream

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "dreams")
data class Dream(
    @PrimaryKey(autoGenerate = true)
    val dreamID: Int,
    val date: String,
    val title: String,
    val description: String,
    val tags: List<String>
)