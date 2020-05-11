package com.miraxh.dreamer.ui.add.images

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R

class ImageListAdapter(
    val context: Context,
    val audioList: List<String>,
    val itemListener: AudioListener,
    var mode: Int
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.audio_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = audioList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio = audioList[position]
        with(holder) {

        }
    }

    interface AudioListener {
        fun onAudioItemListener(titleRecording: String, playerIcon: ImageView)
    }
}