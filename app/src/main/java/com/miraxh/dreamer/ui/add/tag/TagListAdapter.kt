package com.miraxh.dreamer.ui.add.tag

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R

class TagListAdapter(
    val context: Context,
    val tags: List<String>,
    val itemListener: TagListener,
    val mode: Int
) : RecyclerView.Adapter<TagListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagBtn = itemView?.findViewById<Button>(R.id.tag_item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tag_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = tags[position]
        with(holder) {
            tagBtn?.text = tag
            if (mode == 1) {
                tagBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_target, 0, 0, 0);
            }

        }

        if (mode == 0) {
            holder.itemView.setOnClickListener {
                itemListener.onTagItemListener(tag, holder.layoutPosition)
            }
        }
    }

    interface TagListener {
        fun onTagItemListener(tag: String, position: Int)
    }
}