package com.miraxh.dreamer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "dreams")
data class Dream(
    @PrimaryKey(autoGenerate = true)
    val dreamID: Int,
    val date: String,
    val title: String,
    val description: String
)