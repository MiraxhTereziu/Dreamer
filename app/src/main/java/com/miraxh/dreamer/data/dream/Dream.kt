package com.miraxh.dreamer.data.dream

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dreams")
data class Dream(
    @PrimaryKey(autoGenerate = true)
    val dreamID: Int,
    val date: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val rate: Float,
    val audioFile: String
)