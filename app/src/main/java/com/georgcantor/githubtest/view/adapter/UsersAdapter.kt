package com.georgcantor.githubtest.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.georgcantor.githubtest.R
import com.georgcantor.githubtest.model.data.UserEntry
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item.view.*
import java.util.ArrayList

class UsersAdapter(private val context: Context) :
    RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private val users: MutableList<UserEntry>?

    init {
        this.users = ArrayList()
    }

    fun setUsers(users: MutableList<UserEntry>) {
        this.users?.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.user_item, null)
        return UsersViewHolder(itemView)
    }

    override fun getItemCount(): Int = users?.size ?: 0

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        Picasso.with(context)
            .load(users?.get(position)?.avatarUrl)
            .into(holder.userImage)
    }

    class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage: ImageView = view.userImageView
    }

}