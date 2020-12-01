package com.miraxh.dreamer.ui.profile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miraxh.dreamer.R
import com.miraxh.dreamer.data.user.User

class UsersListAdapter(
    val context: Context,
    private val users: List<User>,
    private val itemListener: UserListener
) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userPic:ImageView = itemView.findViewById(R.id.user_profile_image)
        val userName:TextView = itemView.findViewById(R.id.user_name)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        with(holder) {

            if (user.profilePic != "null") {
                Glide.with(context)
                    .load(user.profilePic)
                    .into(userPic)
            }
            userName.text = user.name

            holder.itemView.setOnClickListener {
                itemListener.onUserItemListener(user, holder.layoutPosition)
            }
        }
    }

    interface UserListener {
        fun onUserItemListener(selectedUser: User, position: Int)
    }
}