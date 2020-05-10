package com.miraxh.dreamer.ui.add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R

class AudioListAdapter(
    val context: Context,
    val audioList: List<String>,
    val itemListener: AudioListener,
    var mode: Int
) : RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerIcon = itemView?.findViewById<ImageButton>(R.id.player)
        val playerLabel = itemView?.findViewById<TextView>(R.id.audio_label)
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
            //if(audio == "")
            //    holder.itemView?.visibility = View.GONE

            playerLabel.text = "Audio ${(position+1)}"


            holder.itemView.setOnClickListener {
                itemListener.onAudioItemListener(audio,playerIcon,holder)
            }

            if(mode == 0){
                holder.itemView.setOnLongClickListener{
                    itemListener.onAudioItemLongListener(audio,playerIcon,playerLabel,holder)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    interface AudioListener {
        fun onAudioItemListener(titleRecording: String, playerIcon: ImageView, holder: ViewHolder)
        fun onAudioItemLongListener(titleRecording: String, playerIcon: ImageView, playerLabel: TextView, holder: ViewHolder)
    }
}