package com.miraxh.dreamer.ui.home

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.dream.Dream

class DreamListAdapter(
    val context: Context,
    val dreams: List<Dream>
    //val itemListener: DreamListener
) : RecyclerView.Adapter<DreamListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tile = itemView?.findViewById<Button>(R.id.backgroud_tile)
        val date = itemView?.findViewById<TextView>(R.id.date_label)
        val title = itemView?.findViewById<TextView>(R.id.title_label)
        val description = itemView?.findViewById<TextView>(R.id.description_label)
        val rating_display = itemView?.findViewById<TextView>(R.id.rating_display)

        //tags
        val tagwrap = itemView?.findViewById<LinearLayout>(R.id.tag_wrap)
        val tag1 = itemView?.findViewById<Button>(R.id.tag1)
        val tag2 = itemView?.findViewById<Button>(R.id.tag2)
        val tag3 = itemView?.findViewById<Button>(R.id.tag3)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.dream_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dreams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dream = dreams[position]
        with(holder) {
            date?.text = dream.date
            title?.text = dream.title
            description?.text = dream.description
            showTags(dream)

            var rate = dream.rate
            var rateInt: Int = -1

            if((rate%1F)==0F){
                rateInt = rate.toInt()
                rating_display.text = rateInt.toString()
            }else{
                rating_display.text = rate.toString()
            }
        }
    }

    private fun ViewHolder.showTags(dream: Dream) {
        val textTag1 = dream.tags[0]
        if (textTag1 != "") {
            description.maxLines = 3
            tag1.visibility = View.VISIBLE
            tag1.text = textTag1
            val textTag2 = dream.tags[1]
            if (textTag2 != "") {
                tag2.visibility = View.VISIBLE
                tag2.text = textTag2
                val textTag3 = dream.tags[2]
                if (textTag3 != "") {
                    tag3.visibility = View.VISIBLE
                    tag3.text = textTag3
                } else {
                    tag3.visibility = View.INVISIBLE
                }
            } else {
                tag2.visibility = View.INVISIBLE
                tag3.visibility = View.INVISIBLE
            }
        } else {
            description.maxLines = 5
            tagwrap.visibility = View.GONE
        }
    }
}