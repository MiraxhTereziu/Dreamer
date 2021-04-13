package com.miraxh.dreamer.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.dream.Dream

class DreamListAdapter(
    val context: Context,
    private val dreams: List<Dream>,
    private val itemListener: DreamListener
) : RecyclerView.Adapter<DreamListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date:TextView = itemView.findViewById(R.id.date_label)
        val title:TextView = itemView.findViewById(R.id.title_label)
        val description:TextView = itemView.findViewById(R.id.description_label)
        val ratingDisplay:TextView = itemView.findViewById(R.id.rating_display)

        //tags
        val tag1:Button = itemView.findViewById(R.id.tag1)
        val tag2:Button = itemView.findViewById(R.id.tag2)
        val tag3:Button = itemView.findViewById(R.id.tag3)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dream_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dreams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dream = dreams[position]
        with(holder) {
            date.text = dream.date
            title.text = dream.title
            if(dream.description.isBlank().not())
                description.text = dream.description
            else
                description.text = "Audio description available"
            showTags(dream.tags)
            setRating(dream)

            holder.itemView.setOnClickListener {
                itemListener.onDreamItemListener(dream, holder.layoutPosition)
            }
        }
    }

    private fun ViewHolder.setRating(dream: Dream) {
        val rate = dream.rate
        var rateInt: Int = -1

        if ((rate % 1F) == 0F) {
            rateInt = rate.toInt()
            ratingDisplay.text = rateInt.toString()
        } else {
            ratingDisplay.text = rate.toString()
        }
    }

    private fun ViewHolder.showTags(tagList: MutableList<String>) {
        when (tagList.size) {
            0 -> {
                description.maxLines = 5
                tag1.visibility = View.GONE
                tag2.visibility = View.GONE
                tag3.visibility = View.GONE
            }
            1 -> {
                description.maxLines = 3
                tag1.visibility = View.VISIBLE
                tag1.text = tagList[0]
                tag2.visibility = View.INVISIBLE
                tag3.visibility = View.INVISIBLE
            }
            2 ->{
                description.maxLines = 3
                tag1.visibility = View.VISIBLE
                tag1.text = tagList[0]
                tag2.visibility = View.VISIBLE
                tag2.text = tagList[1]
                tag3.visibility = View.INVISIBLE
            }
            else ->{
                description.maxLines = 3
                tag1.visibility = View.VISIBLE
                tag1.text = tagList[0]
                tag2.visibility = View.VISIBLE
                tag2.text = tagList[1]
                tag3.visibility = View.VISIBLE
                tag3.text = tagList[2]
            }
        }
    }

    interface DreamListener {
        fun onDreamItemListener(dream: Dream, position: Int)
    }
}