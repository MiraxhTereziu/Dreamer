package com.miraxh.dreamer.data.dream

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

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
) : Serializable {
    override fun toString(): String {
        return "[$dreamID\n" +
                "$date\n" +
                "$title\n" +
                "$description\n" +
                "$rate\n" +
                "$audios\n" +
                "$images ]"
    }

    companion object{

        fun getBasePath(context: Context, folderName: String): String {
            return context.getExternalFilesDir(null)?.absolutePath + "/$folderName/"
        }

        fun createUniqueName(): String {
            var titleRecording: String
            val cal = Calendar.getInstance()
            titleRecording = cal.time.toString()
            //trimming della string
            titleRecording = titleRecording.replace(' ', '_')
            titleRecording = titleRecording.replace(':', '_')
            titleRecording = titleRecording.replace('+', '_')
            return titleRecording.toLowerCase(Locale.ROOT)
        }
    }
}