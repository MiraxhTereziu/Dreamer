package com.miraxh.dreamer.ui.toolbar

import android.content.Context
import android.os.Parcelable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.miraxh.dreamer.R
import com.miraxh.dreamer.models.Day

class ToolbarRecycleAdapter(
    val context: Context,
    val days: List<Day>,
    val itemListener: DayListener
) :
    RecyclerView.Adapter<ToolbarRecycleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day = itemView?.findViewById<TextView>(R.id.day)
        val week = itemView?.findViewById<TextView>(R.id.week)
        val active = itemView?.findViewById<Button>(R.id.active)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.date_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = days[position]
        with(holder) {
            day?.text = date.day.toString()
            week?.text = date.dayOfWeek
            if (date.active.not()) {
                active?.visibility = View.INVISIBLE
                day?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorIcon
                    )
                )
                week?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorIcon
                    )
                )

            } else {
                active?.visibility = View.VISIBLE
                day?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
                week?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            }

            holder.itemView.setOnClickListener{

                itemListener.onDayItemListener(date,holder.layoutPosition)
            }
        }
    }

    interface DayListener {
        fun onDayItemListener(day: Day, position: Int)
    }
}

