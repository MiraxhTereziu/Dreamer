package com.miraxh.dreamer.data.dream

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.MediaController
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.miraxh.dreamer.R
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

    companion object {

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

        fun showSaveDialog(context: Context, navController: NavController): Boolean {
            var toRtn = false
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_save)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val negativeAction = dialog.findViewById<Button>(R.id.negative_action)
            val positiveAction = dialog.findViewById<Button>(R.id.positive_action)
            negativeAction.setOnClickListener {
                dialog.dismiss()
                navController.navigate(R.id.home_dest)
                toRtn = false
            }
            positiveAction.setOnClickListener {
                dialog.dismiss()
                toRtn = true
            }
            dialog.show()
            return toRtn
        }
    }
}