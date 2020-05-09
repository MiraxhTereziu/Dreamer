package com.miraxh.dreamer.ui.add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R

class AudioListAdapter(
    val context: Context,
    val audioList: List<String>,
    val itemListener: AudioListener
) : RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val tagBtn = itemView?.findViewById<Button>(R.id.tag_item)
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

        holder.itemView.setOnClickListener {
            itemListener.onAudioItemListener(audio, holder.layoutPosition)
        }
    }

    interface AudioListener {
        fun onAudioItemListener(tag: String, position: Int)
    }
}