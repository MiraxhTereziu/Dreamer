package com.miraxh.dreamer.ui.add.images

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.util.FOLDER_IMAGE

class ImageListAdapter(
    val context: Context,
    private val imageList: List<String>
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePreview:ImageView = itemView.findViewById(R.id.preview_image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_image_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageName = imageList[position]
        with(holder) {
            Glide.with(context)
                .load("${Dream.getBasePath(context, FOLDER_IMAGE)}$imageName")
                .into(imagePreview)
        }
    }

    interface ImageListener {
        fun onImageItemListener(titleRecording: String, playerIcon: ImageView)
    }
}