package com.miraxh.dreamer.data.dream

import android.provider.MediaStore
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dreams")
data class Dream(
    @PrimaryKey(autoGenerate = true)
    val dreamID: Int,
    val date: String,
    val title: String,
    val description: String,
    val tags: MutableList<String>,
    val rate: Float,
    val audios: MutableList<String>,
    val images: MutableList<String>
):Serializable{
    override fun toString(): String {
        val toRtn = "[$dreamID\n" +
                "$date\n" +
                "$title\n" +
                "$description\n" +
                "$rate\n" +
                "$audios\n" +
                "$images ]"

        return toRtn
    }
}