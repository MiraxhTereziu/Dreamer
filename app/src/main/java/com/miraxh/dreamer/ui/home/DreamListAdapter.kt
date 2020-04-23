package com.miraxh.dreamer.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.Dream
import java.security.AccessController.getContext

class DreamListAdapter(
    val context: Context,
    val dreams: List<Dream>
    //val itemListener: DreamListener
): RecyclerView.Adapter<DreamListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tile = itemView?.findViewById<Button>(R.id.backgroud_tile)
        val date = itemView?.findViewById<TextView>(R.id.date_label)
        val title = itemView?.findViewById<TextView>(R.id.title_label)
        val description = itemView?.findViewById<TextView>(R.id.description_label)
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
        }
    }
}